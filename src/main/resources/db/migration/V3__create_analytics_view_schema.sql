DROP
MATERIALIZED VIEW IF EXISTS music_analytics_data_view;

create materialized view music_analytics_data_view as
WITH daily_stats AS (SELECT s.organization_id,
                            sad.snapshot_date,
                            sum(sad.play_minutes) AS daily_minutes
                     FROM store_analytics_data sad
                              JOIN stores s ON sad.store_name::text = s.name::text
                     GROUP BY s.organization_id, sad.snapshot_date),
     weekly_map AS (SELECT daily_stats.organization_id,
                           jsonb_object_agg(to_char(daily_stats.snapshot_date::timestamp with time zone, 'Dy'::text),
                                            daily_stats.daily_minutes) AS weekly_plays_map
                    FROM daily_stats
                    WHERE daily_stats.snapshot_date >= (CURRENT_DATE - '7 days'::interval)
                    GROUP BY daily_stats.organization_id),
     growth_calc AS (SELECT daily_stats.organization_id,
                            sum(daily_stats.daily_minutes)
                            FILTER (WHERE daily_stats.snapshot_date >= (CURRENT_DATE - '7 days'::interval))  AS total_now,
                            sum(daily_stats.daily_minutes)
                            FILTER (WHERE daily_stats.snapshot_date < (CURRENT_DATE - '7 days'::interval) AND
                                          daily_stats.snapshot_date >=
                                          (CURRENT_DATE - '14 days'::interval))                              AS total_prev
                     FROM daily_stats
                     GROUP BY daily_stats.organization_id),
     top_store_per_org AS (SELECT DISTINCT ON (s.organization_id) s.organization_id,
                                                                  sad.store_name,
                                                                  sum(sad.play_minutes) AS ts_total
                           FROM store_analytics_data sad
                                    JOIN stores s ON sad.store_name::text = s.name::text
                           WHERE sad.snapshot_date >= (CURRENT_DATE - '7 days'::interval)
                           GROUP BY s.organization_id, sad.store_name
                           ORDER BY s.organization_id, (sum(sad.play_minutes)) DESC)
SELECT ob.id,
       ob.id                                                                                      AS organization_id,
       CURRENT_DATE                                                                               AS snapshot_date,
       COALESCE(wm.weekly_plays_map, '{}'::jsonb)                                                 AS weekly_plays,
       COALESCE(gc.total_now, 0)::integer                                                         AS total_plays,
       CASE
           WHEN COALESCE(gc.total_prev, 0::numeric) = 0::numeric AND COALESCE(gc.total_now, 0::numeric) > 0::numeric
               THEN 100
           WHEN COALESCE(gc.total_prev, 0::numeric) > 0::numeric THEN (
               (gc.total_now::double precision - gc.total_prev::double precision) * 100::double precision /
               gc.total_prev::double precision)::integer
           ELSE 0
           END                                                                                    AS total_plays_growth_percentage,
       round(COALESCE(gc.total_now, 0::numeric)::double precision /
             7::double precision)::integer                                                        AS average_daily_plays,
       COALESCE(tsp.store_name, 'N/A'::character varying)                                         AS top_store_name,
       0                                                                                          AS top_store_plays_growth_percentage
FROM organizations ob
         LEFT JOIN weekly_map wm ON ob.id = wm.organization_id
         LEFT JOIN growth_calc gc ON ob.id = gc.organization_id
         LEFT JOIN top_store_per_org tsp ON ob.id = tsp.organization_id;

alter materialized view music_analytics_data_view owner to postgres;

create unique index idx_music_analytics_org_id
    on music_analytics_data_view (id);



DROP
MATERIALIZED VIEW IF EXISTS store_aggregate_analytics_data_view;

CREATE
MATERIALIZED VIEW store_aggregate_analytics_data_view AS
WITH slot_with_org AS (
    SELECT
        st.organization_id,
        js.status,
        js.jingle_id,
        js.created_at
    FROM jingle_slots js
             JOIN jingle_schedules jsch ON js.jingle_schedule_id = jsch.id
             JOIN stores st ON jsch.store_id = st.id
),
     org_counts AS (
         SELECT
             organization_id,
             COUNT(*) FILTER (WHERE status = 'PLAYED') as total_played,
             COUNT(*) FILTER (WHERE status = 'PLAYED' AND created_at >= CURRENT_DATE - INTERVAL '7 days') as played_this_week,
             COUNT(*) FILTER (WHERE status = 'PLAYED' AND created_at < CURRENT_DATE - INTERVAL '7 days'
                 AND created_at >= CURRENT_DATE - INTERVAL '14 days') as played_last_week,
             COUNT(*) FILTER (WHERE status = 'PLAYED') * 100 /
             NULLIF(COUNT(*) FILTER (WHERE status IN ('PLAYED', 'MISSED', 'FAILED')), 0) as rate
         FROM slot_with_org
         GROUP BY organization_id
     ),
     top_jingle_per_org AS (
         SELECT DISTINCT ON (organization_id)
             organization_id, announcement_text, play_count
         FROM (
                  SELECT swo.organization_id, j.announcement_text, COUNT(*) as play_count
                  FROM slot_with_org swo
                           JOIN jingles j ON swo.jingle_id = j.id
                  WHERE swo.status = 'PLAYED'
                  GROUP BY swo.organization_id, j.announcement_text
              ) sub ORDER BY organization_id, play_count DESC
     ),
     top_task_per_org AS (
         SELECT DISTINCT ON (organization_id)
             organization_id, task_name, task_count
         FROM (
                  SELECT swo.organization_id, j.category::text as task_name, COUNT(*) as task_count
                  FROM slot_with_org swo
                           JOIN jingles j ON swo.jingle_id = j.id
                  GROUP BY swo.organization_id, j.category
              ) sub ORDER BY organization_id, task_count DESC
     )
SELECT ob.id                        as id,
       ob.id                        as organization_id,
       CURRENT_DATE                 as snapshot_date,
       COALESCE(oc.total_played, 0) as total_jingle_broadcasts,
       CASE
           WHEN COALESCE(oc.played_last_week, 0) = 0 AND COALESCE(oc.played_this_week, 0) > 0 THEN 100
           WHEN COALESCE(oc.played_last_week, 0) > 0
               THEN ((oc.played_this_week::float - oc.played_last_week::float) * 100 / oc.played_last_week::float)::int
        ELSE 0
END
as total_jingle_broadcasts_week_growth,
    COALESCE(oc.rate, 0) as completion_rate,
    COALESCE(tj.announcement_text, 'No data') as most_played_jingle_name,
    COALESCE(tj.play_count, 0) as most_played_jingle_play_count,
    COALESCE(tt.task_name, 'No data') as scheduled_jingle_task_name,
    COALESCE(tt.task_count, 0) as scheduled_jingle_task_count
FROM organizations ob
         LEFT JOIN org_counts oc ON ob.id = oc.organization_id
         LEFT JOIN top_jingle_per_org tj ON ob.id = tj.organization_id
         LEFT JOIN top_task_per_org tt ON ob.id = tt.organization_id
WITH DATA;

CREATE UNIQUE INDEX idx_store_aggregate_org_id ON store_aggregate_analytics_data_view (id);


DROP
MATERIALIZED VIEW IF EXISTS jingle_aggregate_analytics_data_view;

create materialized view jingle_aggregate_analytics_data_view as
WITH organization_base AS (SELECT organizations.id AS org_id
                           FROM organizations),
     slot_data AS (SELECT s.organization_id,
                          js.status,
                          js.jingle_id,
                          js.created_at,
                          js.play_time
                   FROM jingle_slots js
                            JOIN jingle_schedules jsch ON js.jingle_schedule_id = jsch.id
                            JOIN stores s ON jsch.store_id = s.id),
     org_counts AS (SELECT slot_data.organization_id,
                           count(*) FILTER (WHERE slot_data.status::text = 'PLAYED'::text) AS                                         total_played,
                           count(*) FILTER (WHERE slot_data.status::text = 'PLAYED'::text AND slot_data.created_at >=
                                                                                              (CURRENT_DATE - '7 days'::interval)) AS played_this_week,
                           count(*) FILTER (WHERE slot_data.status::text = 'PLAYED'::text AND
                                                  slot_data.created_at < (CURRENT_DATE - '7 days'::interval) AND
                                                  slot_data.created_at >=
                                                  (CURRENT_DATE - '14 days'::interval)) AS                                            played_last_week,
                           count(*) FILTER (WHERE slot_data.status::text = 'PLAYED'::text) * 100 / NULLIF(count(*)
                                                                                                          FILTER (WHERE
                                                                                                              slot_data.status::text = ANY
                                                                                                              (ARRAY ['PLAYED'::character varying, 'MISSED'::character varying, 'FAILED'::character varying]::text[])),
                                                                                                          0) AS                       rate
                    FROM slot_data
                    GROUP BY slot_data.organization_id),
     top_jingles AS (SELECT DISTINCT ON (sub.organization_id) sub.organization_id,
                                                              sub.announcement_text,
                                                              sub.cnt
                     FROM (SELECT sd.organization_id,
                                  j.announcement_text,
                                  count(*) AS cnt
                           FROM slot_data sd
                                    JOIN jingles j ON sd.jingle_id = j.id
                           WHERE sd.status::text = 'PLAYED'::text
                           GROUP BY sd.organization_id, j.announcement_text) sub
                     ORDER BY sub.organization_id, sub.cnt DESC),
     upcoming_tasks AS (SELECT sub_p.organization_id,
                               first_value(sub_p.announcement_text)
                               OVER (PARTITION BY sub_p.organization_id ORDER BY sub_p.play_time)  AS nearest_jingle_name,
                               count(*)
                               FILTER (WHERE sub_p.status::text = 'PENDING'::text AND sub_p.play_time >= now() AND
                                             sub_p.play_time <
                                             (CURRENT_DATE + '1 day'::interval))                   AS remaining_today_count
                        FROM (SELECT sd.organization_id,
                                     sd.status,
                                     sd.play_time,
                                     j.announcement_text
                              FROM slot_data sd
                                       JOIN jingles j ON sd.jingle_id = j.id
                              WHERE sd.status::text = 'PENDING'::text
                                AND sd.play_time >= now()) sub_p
                        GROUP BY sub_p.organization_id, sub_p.announcement_text, sub_p.play_time)
SELECT ob.org_id                                                    AS id,
       ob.org_id                                                    AS organization_id,
       CURRENT_DATE                                                 AS snapshot_date,
       COALESCE(oc.total_played, 0::bigint)                         AS total_jingle_broadcasts,
       CASE
           WHEN COALESCE(oc.played_last_week, 0::bigint) = 0 AND COALESCE(oc.played_this_week, 0::bigint) > 0 THEN 100
           WHEN COALESCE(oc.played_last_week, 0::bigint) > 0 THEN (
               (oc.played_this_week::double precision - oc.played_last_week::double precision) * 100::double precision /
               oc.played_last_week::double precision)::integer
           ELSE 0
           END                                                      AS total_jingle_broadcasts_week_growth,
       COALESCE(oc.rate, 0::bigint)                                 AS completion_rate,
       COALESCE(tj.announcement_text, 'No data'::character varying) AS most_played_jingle_name,
       COALESCE(tj.cnt, 0::bigint)                                  AS most_played_jingle_play_count,
       COALESCE((SELECT ut.nearest_jingle_name
                 FROM upcoming_tasks ut
                 WHERE ut.organization_id = ob.org_id
                 LIMIT 1), 'None today'::character varying)         AS scheduled_jingle_task_name,
       COALESCE((SELECT sum(ut.remaining_today_count) AS sum
                 FROM upcoming_tasks ut
                 WHERE ut.organization_id = ob.org_id), 0)::integer AS scheduled_jingle_task_count
FROM organization_base ob
         LEFT JOIN org_counts oc ON ob.org_id = oc.organization_id
         LEFT JOIN top_jingles tj ON ob.org_id = tj.organization_id;

alter materialized view jingle_aggregate_analytics_data_view owner to postgres;

create unique index idx_jingle_aggregate_org_id
    on jingle_aggregate_analytics_data_view (id);

DROP
MATERIALIZED VIEW IF EXISTS jingle_types_distribution_data_view;

CREATE MATERIALIZED VIEW jingle_types_distribution_data_view AS
SELECT
    ROW_NUMBER() OVER () AS id,
        j.organization_id,
    CURRENT_DATE AS snapshot_date, -- Более лаконичная запись для LocalDate
    j.category,
    COUNT(*) AS total_plays,
    ROUND(
            COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (PARTITION BY j.organization_id),
            1
    )::integer AS percentage -- Приводим к integer, если в Java поле int
FROM jingle_slots js
         JOIN jingles j ON js.jingle_id = j.id
WHERE js.status = 'PLAYED'
  AND js.play_time >= DATE_TRUNC('week', NOW())
  AND js.play_time < DATE_TRUNC('week', NOW()) + INTERVAL '7 days'
GROUP BY j.organization_id, j.category
ORDER BY j.organization_id, total_plays DESC
WITH DATA;

CREATE UNIQUE INDEX idx_jingle_types_distribution_data_view
    ON jingle_types_distribution_data_view (id);