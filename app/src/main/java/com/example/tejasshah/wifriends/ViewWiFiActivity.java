package com.example.tejasshah.wifriends;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiConfiguration;
        import android.net.wifi.WifiManager;
        import android.opengl.Visibility;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.tejasshah.wifriends.models.Networks;

        import java.util.ArrayList;
        import java.util.List;

public class ViewWiFiActivity extends AppCompatActivity {
    private ListView lvNetworks;
    WifiManager wifiManager;
    WifiReceiver wifiRec = new WifiReceiver();
    List<Networks> NetworkAvail = new ArrayList<Networks>();
    String wname,wpass;
    TextView tvwifiStat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wi_fi);

        tvwifiStat = (TextView)findViewById(R.id.tvWifiSearchStatus);
        lvNetworks = (ListView)findViewById(R.id.lvNetworkAv);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String email = intent.getStringExtra("email");
        final String name = intent.getStringExtra("name");
        final String wid = intent.getStringExtra("username");
        wname = intent.getStringExtra("wname");
        wpass = intent.getStringExtra("wpass");
        final String name1 = intent.getStringExtra("name1");


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiRec, filter);
        lvNetworks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Networks netObj = NetworkAvail.get(position);

                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = "\"" + netObj.getSsid() + "\"";
                wc.BSSID = netObj.getBssid();
                wc.preSharedKey = "\"" + wpass + "\"";

                int netid = wifiManager.addNetwork(wc);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netid, true);
                wifiManager.reconnect();

            }
        });


    }

    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent)
        {
            List<WifiConfiguration> results = wifiManager.getConfiguredNetworks();
            Toast.makeText(getBaseContext(),String.valueOf(results.size()),Toast.LENGTH_SHORT).show();
            final List<ScanResult> scanResults = wifiManager.getScanResults();
            Toast.makeText(getBaseContext(),String.valueOf(scanResults.size()),Toast.LENGTH_SHORT).show();
            unregisterReceiver(wifiRec);
            for(ScanResult result: scanResults){
                if((!result.SSID.isEmpty()) && (result.SSID.equals(wname))){
                    Networks netObj = new Networks(result.SSID,result.BSSID,result.capabilities,result.level);
                    NetworkAvail.add(netObj);
                }

            }

            ArrayAdapter<Networks> networkAdp = new ArrayAdapter<Networks>(getBaseContext(), android.R.layout.simple_list_item_2, android.R.id.text1,NetworkAvail){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =  super.getView(position, convertView, parent);
                    Networks netObj = NetworkAvail.get(position);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(netObj.getSsid());
                    text2.setText(netObj.getCapability());
                    return view;
                }
            };
            lvNetworks.setAdapter(networkAdp);
            if(NetworkAvail.size() > 0)
            {
                tvwifiStat.setVisibility(View.GONE);
            }
            else {
                tvwifiStat.setText("No Wireless Connections to connect");
            }
        }


    }




}

