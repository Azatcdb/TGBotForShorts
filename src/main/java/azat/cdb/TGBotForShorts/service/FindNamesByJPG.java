package azat.cdb.TGBotForShorts.service;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
@Repository
public interface FindNamesByJPG {
    public String getJPG(String url) throws IOException;
    public String getNamesByImg(String urlImg) throws IOException;
}
