package edu.cwru.students.cwrumapper;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Location;

public class RouterTest {

    @Test
    public void testRouter() {

        String apiKey = "AIzaSyDNbP-3OzqyB2PfR4_XElCfdhAtRCfNC2o";

        // location data taken from Location.populateData()
        Location clarke = new Location("Clarke", new LatLng[]{
                new LatLng(41.514455, -81.605709)});
        Location taft = new Location("Taft", new LatLng[]{
                new LatLng(41.512756, -81.607186)});
        Location tink = new Location("Tinkham Veale", new LatLng[]{
                new LatLng(41.508757, -81.608493),
                new LatLng(41.507596, -81.608756)});
        Location millisSchmitt = new Location("Millis Schmitt", new LatLng[]{
                new LatLng(41.504099, -81.606873),
                new LatLng(41.503729, -81.607005)});

        // no events
        DayItinerary zeroItin = new DayItinerary();
        Assert.assertNull(Router.findRoute(zeroItin, apiKey));

        // one event
        DayItinerary oneItin = new DayItinerary();
        oneItin.addEvent(new Event("Test", tink, 1, "", 12, 0, 0));
        Assert.assertNull(Router.findRoute(oneItin, apiKey));

        // two events, same location
        DayItinerary twoSameItin = new DayItinerary();
        twoSameItin.addEvent(new Event("Test1", tink, 1, "", 12, 0, 0));
        twoSameItin.addEvent(new Event("Test2", tink, 1, "", 13, 0, 0));
        ArrayList<ArrayList<LatLng>> twoSameRoute = Router.findRoute(twoSameItin, apiKey);
        Assert.assertNotNull(twoSameRoute);
        Assert.assertEquals(twoSameRoute.size(), 1);

        // two events, different locations
        DayItinerary twoDiffItin = new DayItinerary();
        twoDiffItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        twoDiffItin.addEvent(new Event("Test2", tink, 1, "", 13, 0, 0));
        ArrayList<ArrayList<LatLng>> twoDiffRoute = Router.findRoute(twoDiffItin, apiKey);
        Assert.assertNotNull(twoDiffRoute);
        Assert.assertEquals(twoDiffRoute.size(), 1);

        // three events, no partitions
        DayItinerary threeNoPartItin = new DayItinerary();
        threeNoPartItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        threeNoPartItin.addEvent(new Event("Test2", taft, 1, "", 13, 0, 0));
        threeNoPartItin.addEvent(new Event("Test3", tink, 1, "", 14, 0, 0));
        ArrayList<ArrayList<LatLng>> threeNoPartRoute = Router.findRoute(threeNoPartItin, apiKey);
        Assert.assertNotNull(threeNoPartRoute);
        Assert.assertEquals(threeNoPartRoute.size(), 2);

        // three events, partitions
        DayItinerary threePartItin = new DayItinerary();
        threePartItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        threePartItin.addEvent(new Event("Test2", tink, 1, "", 13, 0, 0));
        threePartItin.addEvent(new Event("Test3", millisSchmitt, 1, "", 14, 0, 0));
        ArrayList<ArrayList<LatLng>> threePartRoute = Router.findRoute(threePartItin, apiKey);
        Assert.assertNotNull(threePartRoute);
        Assert.assertEquals(threePartRoute.size(), 2);

        // four events, circuit
        DayItinerary fourCircItin = new DayItinerary();
        fourCircItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        fourCircItin.addEvent(new Event("Test2", taft, 1, "", 13, 0, 0));
        fourCircItin.addEvent(new Event("Test3", tink, 1, "", 14, 0, 0));
        fourCircItin.addEvent(new Event("Test4", clarke, 1, "", 15, 0, 0));
        ArrayList<ArrayList<LatLng>> fourCircRoute = Router.findRoute(fourCircItin, apiKey);
        Assert.assertNotNull(fourCircRoute);
        Assert.assertEquals(fourCircRoute.size(), 3);

        // four events, consecutive repeat
        DayItinerary fourRepItin = new DayItinerary();
        fourRepItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        fourRepItin.addEvent(new Event("Test2", taft, 1, "", 13, 0, 0));
        fourRepItin.addEvent(new Event("Test3", taft, 1, "", 14, 0, 0));
        fourRepItin.addEvent(new Event("Test4", tink, 1, "", 15, 0, 0));
        ArrayList<ArrayList<LatLng>> fourRepRoute = Router.findRoute(fourRepItin, apiKey);
        Assert.assertNotNull(fourRepRoute);
        Assert.assertEquals(fourRepRoute.size(), 3);

        // four events, non-consecutive repeat
        DayItinerary fourNRepItin = new DayItinerary();
        fourNRepItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        fourNRepItin.addEvent(new Event("Test2", taft, 1, "", 13, 0, 0));
        fourNRepItin.addEvent(new Event("Test3", clarke, 1, "", 14, 0, 0));
        fourNRepItin.addEvent(new Event("Test4", tink, 1, "", 15, 0, 0));
        ArrayList<ArrayList<LatLng>> fourNRepRoute = Router.findRoute(fourNRepItin, apiKey);
        Assert.assertNotNull(fourNRepRoute);
        Assert.assertEquals(fourNRepRoute.size(), 3);

        // four events, different
        DayItinerary fourDiffItin = new DayItinerary();
        fourDiffItin.addEvent(new Event("Test1", clarke, 1, "", 12, 0, 0));
        fourDiffItin.addEvent(new Event("Test2", taft, 1, "", 13, 0, 0));
        fourDiffItin.addEvent(new Event("Test3", tink, 1, "", 14, 0, 0));
        fourDiffItin.addEvent(new Event("Test4", millisSchmitt, 1, "", 15, 0, 0));
        ArrayList<ArrayList<LatLng>> fourDiffRoute = Router.findRoute(fourDiffItin, apiKey);
        Assert.assertNotNull(fourDiffRoute);
        Assert.assertEquals(fourDiffRoute.size(), 3);

        // invalid key
        Assert.assertNull(Router.findRoute(fourDiffItin, "asdfghjkl"));
    }
}
