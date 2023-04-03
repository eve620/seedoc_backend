package top.shlande.clouddisk.storage;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CompleteUploadResult {
    public String etag;
    public Long size;
}
