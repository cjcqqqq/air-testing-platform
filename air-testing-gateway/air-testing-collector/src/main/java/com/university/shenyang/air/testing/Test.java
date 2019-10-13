package com.university.shenyang.air.testing;


import com.mercator.TileUtils;
import com.vector.tile.VectorTileDecoder;
import com.vector.tile.VectorTileEncoder;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.el.parser.ParseException;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.List;
import java.util.Map;

public class Test {
    private boolean isWMTS = false;
    public static void main(String[] args) {
        Test a = new Test();
        String pbf = "GrQBCglXYXRlcmZhY2USGxIGAAABAQICGAMiDwk/wEAaALkQugoAALoQDxIbEgYAAAEBAgIYAyIPCfoJPxoAxjC5CgAAxTAPEhwSBgAAAQECAhgDIhAJwECGMBrFNgAAxTDGNgAPEhwSBgAAAQECAhgDIhAJ+gnAQBoAuRDGNgAAuhAPGg1kaXNwbGF5X2NsYXNzGgRraW5kGgphcmVhX2xldmVsIgIwECICMAIiAzDIASiAIHgC";
        byte[] mapdata = Base64.decodeBase64(pbf);
        a.dataReEncode(mapdata);
    }
//    private byte[] dataReEncode(byte[] data, int z, int x, int y) {
    private byte[] dataReEncode(byte[] data) {

        VectorTileEncoder vtEncoder = new VectorTileEncoder(4096, 16, false);

        List<VectorTileDecoder.Feature> featureList = decodeEncodedPbf(data);

        for (VectorTileDecoder.Feature feature : featureList) {
            System.out.println("getExtent:"+feature.getExtent());
            System.out.println("getGeometry:"+feature.getGeometry());
            System.out.println("getLayerName:"+feature.getLayerName());

            Map<String, Object> attr = feature.getAttributes();
            for(String key :attr.keySet()) {
                System.out.println(key+":"+attr.get(key));
            }
            if (!attr.containsKey("wkt")) {
                return data;
            }
//            try {
//                Geometry geom = new WKTReader().read((String)attr.get("wkt"));
//                if (!this.isWMTS) {
//                    TileUtils.convert2Piexl(x, y, z, geom);
//                } else
//                    TileUtils.convert2PiexlWMTS(x, y, z, geom);
//                vtEncoder.addFeature(feature.getLayerName(), attr, geom);
//            } catch (ParseException e) {
//                e.printStackTrace();
//                return null;
//            }
        }

        return vtEncoder.encode();
    }

    private List<VectorTileDecoder.Feature> decodeEncodedPbf(byte[] data) {
        if ((data == null) || (data.length == 0)) {
            return null;
        }
        try {
            VectorTileDecoder d = new VectorTileDecoder();
            d.setAutoScale(false);

            return d.decode(data).asList();
        } catch (Exception e) {
            e.printStackTrace(); }
        return null;
    }
}
