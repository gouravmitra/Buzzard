package buzzard.utility;

import java.io.File;
import java.io.IOException;
import org.neo4j.kernel.impl.util.FileUtils;

public class FileService {
	
	private FileService()
	{
		
	}

	public static void deleteFileOrDirectory( final File file )
    {
        if ( !file.exists() )
        {
            return;
        }

        if ( file.isDirectory() )
        {
            for ( File child : file.listFiles() )
            {
                deleteFileOrDirectory( child );
            }
        }
        else
        {
            file.delete();
        }
    }
	
	public static void clearDb(final String DB_PATH )
    {
        try
        {
            FileUtils.deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }
}
