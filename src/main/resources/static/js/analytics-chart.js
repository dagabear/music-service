(function () {
    Chart.register(ChartDataLabels);

    const ACCENT = 'hsl(225, 85%, 60%)'; // Тот самый синий из макета
    const GRAY   = '#E0E0E0';
    const MUTED  = '#757575';

    // Порядок дней для графика
    const daysOrder = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

    // Подготовка данных для Weekly Plays
    // weeklyPlaysData — это объект { "Monday": 100, "Tuesday": 150 ... }
    const playsLabels = daysOrder;
    const playsValues = daysOrder.map(day => weeklyPlaysData[day] || 0);
    const maxVal = Math.max(...playsValues);

    // 1. Weekly Plays Chart
    new Chart(document.getElementById('weeklyPlaysChart'), {
        type: 'bar',
        data: {
            labels: playsLabels.map(d => d.substring(0, 3)), // Сокращаем до Mon, Tue
            datasets: [{
                data: playsValues,
                backgroundColor: (ctx) => ctx.raw === maxVal ? ACCENT : GRAY,
                borderRadius: 8,
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                datalabels: {
                    anchor: 'end', align: 'end',
                    color: MUTED,
                    formatter: Math.round
                }
            },
            scales: {
                x: { grid: { display: false }, border: { display: false } },
                y: { display: false, suggestMax: maxVal * 1.2 }
            }
        }
    });

    // 2. Jingle Distribution Chart (Doughnut)
    // jingleDistData — это список объектов [{category: 'PROMO', percentage: 45}, ...]
    if (typeof jingleDistData !== 'undefined' && jingleDistData.length > 0) {
        new Chart(document.getElementById('jingleTypeChart'), {
            type: 'doughnut',
            data: {
                labels: jingleDistData.map(d => d.category),
                datasets: [{
                    data: jingleDistData.map(d => d.percentage),
                    backgroundColor: ['#6366F1', '#F59E0B', '#10B981', '#EF4444'],
                    borderWidth: 0
                }]
            },
            options: {
                cutout: '70%',
                plugins: {
                    legend: { position: 'bottom', labels: { usePointStyle: true, padding: 20 } }
                }
            }
        });
    }
})();