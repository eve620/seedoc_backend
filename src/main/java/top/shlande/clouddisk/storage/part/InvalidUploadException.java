package top.shlande.clouddisk.storage.part;

import java.util.UUID;

public class InvalidUploadException extends RuntimeException {
    InvalidUploadException(String uuid) {
        super("invalid upload id: " + uuid);
    }
}
