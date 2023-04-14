package top.shlande.clouddisk.vfs.mysql;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MySQLFileInfoRepository extends CrudRepository<MySQLFileInfo, Long> {
    public MySQLFileInfo getByUploadId(String uploadId);

    @Modifying
    @Query("UPDATE `files` set `parent` = replace(`parent`,:src,:dst) where parent LIKE :#{#src + '%'} ")
    public void moveDir(String src, String dst);

    @Modifying
    @Query("UPDATE `files` set `name` = :nameDst, `parent` = :parentDst where `id` = :id")
    public void moveFile(String parentDst, String nameDst, Long id);

    @Query("SELECT * from files WHERE parent = :parent and name = :name LIMIT 1")
    public MySQLFileInfo getByPath(@Param("parent") String parent, @Param("name") String name);

    @Query("SELECT * from files WHERE parent = :parent AND id > :startAfter AND NOT(created IS NULL) LIMIT :size")
    public List<MySQLFileInfo> list(@Param("parent") String parent, @Param("size") Integer size, @Param("startAfter") Integer startAfter);

    @Query("SELECT * from files WHERE etag = :etag LIMIT 1")
    public MySQLFileInfo getByEtag(@Param("etag") String etag);

    @Modifying
    @Query("UPDATE files SET etag = :etag, size = :size, created = NOW(), last_modified = NOW() WHERE upload_id = :uploadId")
    public void complete(@Param("uploadId") String uploadId, @Param("etag") String etag, @Param("size") Long size);

    @Modifying
    @Query("DELETE FROM files WHERE parent = :parent and name = :name")
    public void deleteByPath(@Param("parent") String parent, @Param("name") String name);

    @Query("SELECT * from files WHERE parent = :parent OR parent like :parentLike")
    public List<MySQLFileInfo> walk(@Param("parent") String parent, @Param("parentLike") String parentLike);
}
