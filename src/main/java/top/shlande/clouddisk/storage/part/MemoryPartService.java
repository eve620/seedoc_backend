package top.shlande.clouddisk.storage.part;


import java.util.*;

public class MemoryPartService implements PartService {
    private final HashMap<UUID, TreeMap<Integer, String>> parts = new HashMap<>();


    @Override
    public String create() throws Exception {
        var uuid = UUID.randomUUID();
        var parts_ = new TreeMap<Integer, String>();
        parts.put(uuid, parts_);
        return uuid.toString();
    }

    @Override
    public void put(String uploadId, String partEtag, int part) {
        var parts_ = this.parts.get(UUID.fromString(uploadId));
        if (parts_ == null) {
            throw new InvalidUploadException(uploadId);
        }
        parts_.put(part, partEtag);
    }

    @Override
    public void delete(String uploadId) {
        this.parts.remove(UUID.fromString(uploadId));
    }

    @Override
    public SortedMap<Integer, String> get(String uploadId) {
        var parts_ = this.parts.get(UUID.fromString(uploadId));
        if (parts_ == null) {
            throw new InvalidUploadException(uploadId);
        }
        return parts_;
    }
}
