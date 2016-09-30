package tw.com.ischool.popularmoviesstage1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevinhuang on 2016/9/29.
 */

public class JSONUtil {

    public static String getValue(JSONObject objJson, String fieldName) throws JSONException {
        String result = "";
        if (objJson != null) {
            if (!objJson.isNull(fieldName)) {
                result = objJson.getString(fieldName);
            }
        }

        return result ;
    }
}
