package loomo.com.awesome.data;

/**
 * Created by MuhammadIqbal on 11/2/2017.
 */

public class MovingTile {

    /***
     * Longitude to tile (x)
     * @param lat
     * @param zoom
     * @return
     */
    public int longToTile(double lat, int zoom) {
       int xtile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;

        if (xtile < 0)
            xtile=0;
        else if (xtile >= (1<<zoom))
            xtile=((1<<zoom)-1);

        return xtile;
    }

    /***
     * Latitude to tile (y)
     * @param lon
     * @param zoom
     * @return
     */
    public int latToTile(double lon, int zoom) {
        int ytile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;

        if (ytile < 0)
            ytile=0;
        else if (ytile >= (1<<zoom))
            ytile=((1<<zoom)-1);

        return ytile;
    }

}
