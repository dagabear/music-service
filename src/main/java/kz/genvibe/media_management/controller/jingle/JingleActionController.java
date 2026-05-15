package kz.genvibe.media_management.controller.jingle;

import jakarta.validation.Valid;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleApproveDto;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.JingleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/jingles")
@RequiredArgsConstructor
public class JingleActionController {

    private final JingleService jingleService;

    @PostMapping
    public String createJingle(
        @CurrentUser AppUser appUser,
        @Valid JingleCreateDto dto
    ) {
        jingleService.createJingle(appUser, dto);
        return "redirect:/jingles";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteJingle(
        @PathVariable Long id,
        @CurrentUser AppUser appUser
    ) {
        jingleService.deleteJingleById(id, appUser);
    }

    @PatchMapping("/{id}/stores")
    @ResponseBody
    public void addJingleToLocations(
        @PathVariable long id,
        @RequestBody List<Long> idList,
        @CurrentUser AppUser appUser
    ) {
        jingleService.addJingleToStores(id, idList, appUser);
    }

    @ResponseBody
    @PatchMapping("/approve-pause-request/{id}")
    public String approveJingle(
        @PathVariable long id,
        @CurrentUser AppUser appUser
    ) {
        jingleService.setPauseApprovalStatus(id, appUser);
        return "redirect:/jingles";
    }

    @ResponseBody
    @PatchMapping("/pause-request/{id}")
    public void requestToPause(@PathVariable long id) {
        jingleService.requestToPauseJingle(id);
    }

}
