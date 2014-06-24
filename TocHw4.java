import java.net.*;
import java.io.*;
import org.json.*;

public class TocHw4 {
    public static void main(String[] args) throws Exception {
		if(args.length == 0) {System.out.println("no URL"); System.exit(1);}
        try {
			URL oracle = new URL(args[0]);

			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			JSONArray jsonRealPrice = new JSONArray(new JSONTokener(in));
			JSONObject json;
			int num = 0, year, yeari, price, hash;
			int[][] index = new int[2000][200];
			String[] road = new String[2000];
			String name;
			
			for(int i=0, j=0; i<jsonRealPrice.length();i++)
			{
				json = jsonRealPrice.getJSONObject(i);
				name = json.optString("土地區段位置或建物區門牌");
				price = Integer.valueOf(json.optString("總價元"));
				if(name.indexOf('大') == name.indexOf('道')-1) name = name.substring(0, name.indexOf('道')+1);
				else if(name.indexOf('路') != -1) name = name.substring(0, name.indexOf('路')+1);
				else if(name.indexOf('街') != -1) name = name.substring(0, name.indexOf('街')+1);
				else if(name.indexOf('巷') != -1) name = name.substring(0, name.indexOf('巷')+1);
				else continue;
				for(j=num-1; j>=-1; j--)
				{
					if(j == -1) {j = num++; road[j] = name; index[j][2] = price; break;}
					else if(name.equalsIgnoreCase(road[j])) break;
					else continue;
				}
				year = Integer.valueOf(json.optString("交易年月"));
				yeari = (104-year/100)*12+12-year%100;
				if(index[j][yeari] == 0) {index[j][yeari] = 1; index[j][0]++;}
				if(price > index[j][1]) index[j][1] = price;
				else if(price < index[j][2]) index[j][2] = price;
			}

			int[] big = new int[100];
			int bigm = 3, n = 0;
			for(int i=0; i<num; i++)
				if(index[i][0] > bigm) {bigm = index[i][0]; big[n=0] = i;}
				else if(index[i][0] == bigm) big[++n] = i;
	
			for(int i=0; i<=n; i++)
				System.out.println(road[big[i]]+", 最高成交價: "+index[big[i]][1]+", 最低成交價: "+index[big[i]][2]);

			in.close();
		} catch(IOException e) {
			System.out.println("URL error");
		}
    }
}