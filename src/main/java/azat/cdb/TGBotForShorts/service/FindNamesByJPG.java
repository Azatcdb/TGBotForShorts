package azat.cdb.TGBotForShorts.service;

import java.io.IOException;
import java.util.List;

public interface FindNamesByJPG {
    public String getJPG(String url) throws IOException;
    public String getNamesByImg(String urlImg) throws IOException;
}
