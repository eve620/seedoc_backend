package top.shlande.clouddisk.storage.part;

import java.util.SortedMap;
import java.util.UUID;

public interface PartService {

    public String create() throws Exception;

    // if uploadId don't exist, InvalidUploadException will throw
    public void put(String uploadId, String partEtag, int part);

    public void delete(String uploadId);

    public SortedMap<Integer, String> get(String uploadId);
}
