package com.isuhuo.kuaibao.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-30.
 */
public class NormalPostRequest extends Request<JSONObject> {
    private Map mMap;

    private Response.Listener<JSONObject> mListener;

    public NormalPostRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> map) {

        super(Request.Method.POST, url, errorListener);

        mListener = listener;

        mMap = map;

    }



    //mMap是已经按照前面的方式,设置了参数的实例

    @Override

    protected Map<String, String> getParams() throws AuthFailureError {

        return mMap;

    }

   /* @Override
    public byte[] getBody() throws AuthFailureError {

        return mMap != null && mMap.size() > 0?this.encodeParameters(mMap, this.getParamsEncoding()):null;
    }
    private byte[] encodeParameters(Map<String, Object> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();

        try {
            Iterator var5 = params.entrySet().iterator();

            while(var5.hasNext()) {
                java.util.Map.Entry uee = (java.util.Map.Entry)var5.next();
                encodedParams.append(URLEncoder.encode((String)uee.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode((String)uee.getValue(), paramsEncoding));
                encodedParams.append('&');
            }

            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var6);
        }
    }*/

    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可

    @Override

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {

            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));



            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {

            return Response.error(new ParseError(e));

        } catch (JSONException je) {

            return Response.error(new ParseError(je));

        }

    }

    @Override

    protected void deliverResponse(JSONObject response) {

        mListener.onResponse(response);

    }
}
