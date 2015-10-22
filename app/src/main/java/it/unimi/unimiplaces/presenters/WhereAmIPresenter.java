package it.unimi.unimiplaces.presenters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimi.unimiplaces.PlacesDataSource;
import it.unimi.unimiplaces.views.WhereAmIViewInterface;

/**
 * WhereAmI Presenter
 */
public class WhereAmIPresenter {

    private WhereAmIViewInterface view;
    private PlacesDataSource placesDataSource;

    public WhereAmIPresenter(PlacesDataSource dataSource, WhereAmIViewInterface view){
        this.placesDataSource   = dataSource;
        this.view               = view;
    }

    public void decodeData(String data){
        /* concrete strategy */
        QRCodeDecodeStrategy decodeStrategy = data.contains(QRCodeDecodeStrategy.BUILDING_SEGMENT) ?
                new BuildingDecodeStrategy() :
                new RoomDecodeStrategy();

        QRCodeDecodeResult res = decodeStrategy.decode(data,this.placesDataSource);

        if( res == null ){
            this.view.onScanError();
            return;
        }

        presentResult(res);
    }

    private void presentResult(QRCodeDecodeResult result){
        if( result.roomId == null ){
            this.view.onBuildingScanSuccess(result.buildingId);
        }else{
            this.view.onRoomScanSuccess(result.buildingId,result.roomId);
        }
    }

    class QRCodeDecodeResult{
        public String buildingId;
        public String roomId;

        public QRCodeDecodeResult(String buildingId){
            this.buildingId = buildingId;
        }
        public QRCodeDecodeResult(String buildingId, String roomId){
            this.buildingId = buildingId;
            this.roomId     = roomId;
        }
    }

    interface QRCodeDecodeStrategy{
        String DOMAIN           = "unimiplaces://";
        String BUILDING_SEGMENT = "building";
        String ROOM_SEGMENT     = "room";
        QRCodeDecodeResult decode(String data, PlacesDataSource dataSource);
    }

    class BuildingDecodeStrategy implements QRCodeDecodeStrategy{
        @Override
        public QRCodeDecodeResult decode(String data, PlacesDataSource dataSource) {
            Pattern pattern = Pattern.compile(DOMAIN+BUILDING_SEGMENT+"/([A-Za-z0-9]+)/");
            Matcher matcher = pattern.matcher(data);

            /* check with regexpr */
            if( matcher.matches() && matcher.groupCount()==1 ){
                String match = matcher.group(1);
                /* check db */
                if( dataSource.buildingExists(match) ) {
                    return new QRCodeDecodeResult(match);
                }
            }
            return null;
        }
    }

    class RoomDecodeStrategy implements QRCodeDecodeStrategy{
        @Override
        public QRCodeDecodeResult decode(String data, PlacesDataSource dataSource) {
            Pattern pattern = Pattern.compile(DOMAIN + ROOM_SEGMENT + "/([A-Za-z0-9]+)/([A-Za-z0-9]+)/");
            Matcher matcher = pattern.matcher(data);
            if( matcher.matches() && matcher.groupCount()==2 ){
                String building = matcher.group(1);
                String room     = matcher.group(2);
                /* check db */
                if( dataSource.placeExists(building,room) ) {
                    return new QRCodeDecodeResult(building,room);
                }

            }
                return null;
        }
    }
}
