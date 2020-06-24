package com.example.customremote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RemoteListJsonConverter {
    public static String remoteListToJson(ArrayList<Remote> rems){
        JSONObject j = new JSONObject();
        try {
            for (Remote r : rems) {
                JSONObject rJson = new JSONObject();
                rJson.put("width", r.getWidth());
                rJson.put("height", r.getHeight());
                JSONArray bJsonArray = new JSONArray();
                for (RemoteButton b : r.getButtons()) {
                    JSONObject bJson = new JSONObject();
                    bJson.put("text", b.getText());
                    bJsonArray.put(bJson);

                    JSONArray baJsonArray = new JSONArray();
                    for (RemoteButtonAction rba : b.getActions()) {
                        JSONObject rbaJson = new JSONObject();
                        rbaJson.put("type", rba.getType());
                        JSONObject params_json = new JSONObject();
                        rbaJson.put("params", params_json);
                        for (Map.Entry<String, String> entry : rba.getParams().entrySet()) {
                            params_json.put(entry.getKey(), entry.getValue());
                        }
                        baJsonArray.put(rbaJson);
                    }
                    bJson.put("actions", baJsonArray);
                }
                rJson.put("buttons", bJsonArray);
                j.put(r.getName(), rJson);
            }
            return String.valueOf(j);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Remote> jsonToRemoteList(String jString){
        try {
            JSONObject j = new JSONObject(jString);
            ArrayList<Remote> r = new ArrayList<>();
            for (Iterator<String> it = j.keys(); it.hasNext(); ) {
                String k = it.next();
                if (j.get(k) instanceof JSONObject) {
                    JSONObject a = (JSONObject) j.get(k);
                    Remote rem = new Remote(k, Integer.parseInt(a.getString("width")), Integer.parseInt(a.getString("height")));
                    JSONArray bJsonArray = a.getJSONArray("buttons");
                    ArrayList<RemoteButton> rb = new ArrayList<>();
                    for (int m = 0; m < bJsonArray.length(); m++) {
                        JSONObject but = bJsonArray.getJSONObject(m);
                        RemoteButton remBut = new RemoteButton(but.getString("text"));
                        rem.addButton(remBut);
                        ArrayList<RemoteButtonAction> acs = new ArrayList<>();

                        JSONArray baJsonArray = but.getJSONArray("actions");
                        for (int n = 0; n < baJsonArray.length(); n++) {
                            JSONObject act = baJsonArray.getJSONObject(n);
                            String type = act.getString("type");

                            RemoteButtonAction ac = new RemoteButtonAction("");
                            ac.setType(type);

                            Map<String, String> params = new HashMap<>();
                            JSONObject parJson = act.getJSONObject("params");
                            for (Iterator<String> it2 = parJson.keys(); it2.hasNext(); ) {
                                String k2 = it2.next();
                                if (parJson.get(k2) instanceof String) {
                                    params.put(k2, (String) parJson.get(k2));
                                }
                            }
                            ac.setParams(params);
                            acs.add(ac);

                        }
                        remBut.setActions(acs);
                        rb.add(remBut);
                    }
                    r.add(rem);
                }
            }
            return r;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
