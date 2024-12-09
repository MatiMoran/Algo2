package aed;

import java.util.ArrayList;
import java.util.List;

public class InternetToolkit {
    public InternetToolkit() {
    }

    public Fragment[] tcpReorder(Fragment[] fragments) {
        for (int i = 1; i < fragments.length; i++) {
            int j = i;
            while (j > 0 && fragments[j-1].compareTo(fragments[j]) > 0) {
                int aux = fragments[j]._id;
                fragments[j]._id = fragments[j - 1]._id;
                fragments[j - 1]._id = aux;
                j = j - 1;
            }
        }

        return fragments;
    }

    public Router[] kTopRouters(Router[] routers, int k, int umbral) {

        MaxHeap heap = new MaxHeap(routers); //O(n) por algo de floyd

        List<Router> res = new ArrayList<>();

        // O(k * log(n))
        while (k > 0 && !heap.EsVacio() && heap.ObtenerMaximo().getTrafico() > umbral) { // k veces en el peor caso
            res.add(heap.ObtenerMaximo()); // O(1)
            heap.BorrarMaximo(); // O(log(n))
            k--; // O(1)
        }

        Router[] resArray = new Router[res.size()]; // O(n)
        resArray = res.toArray(resArray); // O(n)

        return resArray;
    }

    public IPv4Address[] sortIPv4(String[] ipv4) {
        
        List<IPv4Address> ips = new ArrayList<IPv4Address>();
        List<IPv4Address>[] buckets = new List[256];
        
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<IPv4Address>();
        }

        for (int i = 0; i < ipv4.length; i++) {
            ips.add(new IPv4Address(ipv4[i]));
        }
        
        for (int octet = 3; octet >= 0; octet--) {

            for (int i = 0; i < ips.size(); i++) {
                IPv4Address ip = ips.get(i);
                buckets[ip.getOctet(octet)].add(ip);
            }

            ips.clear();

            for (int i = 0; i < buckets.length; i++) {

                for (int j = 0; j < buckets[i].size(); j++) {
                    ips.add(buckets[i].get(j));
                }

                buckets[i].clear();
            }
        }

        return ips.toArray(new IPv4Address[ips.size()]);
    }

}
