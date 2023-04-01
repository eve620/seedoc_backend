package top.shlande.clouddisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.storage.LocalStorageService;

@RequestMapping("meta")
@RestController
public class MetaController {
    private LocalStorageService storageService;

    public MetaController(@Autowired LocalStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String list(@RequestParam String prefix,
                       @RequestParam(required = false,defaultValue = "50") int maxKeys,
                       @RequestParam(required = false,defaultValue = "0") int startAfter) {
        return "hello";
    }

    @PostMapping("/{Key}?upload")
    public void createUpload(@PathVariable String Key) {
        return;
    }


}
