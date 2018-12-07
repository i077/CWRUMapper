package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A class for holding the latitude and longitude coordinates for a particular location.
 * Also contains the name of the location.
 */
@Entity
public class Location {
    @ColumnInfo(name = "LocationName")
    @PrimaryKey
    @NonNull
    private String name;

    // latitudes and longitudes of all entrances to this Location
    @NonNull
    private ArrayList<Double> latitudes;
    @NonNull
    private ArrayList<Double> longitudes;

    @Ignore
    public Location(String name)
    {
        this.name = name;
        latitudes = new ArrayList<Double>();
        longitudes = new ArrayList<Double>();
    }

    public Location(String name, ArrayList<Double> latitudes, ArrayList<Double> longitudes){
        this.name = name;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
	}

    public Location(String n, LatLng[] lls) {
        name = n;

        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        for (int i = 0; i < lls.length; i++) {
            LatLng e = lls[i];
            latitudes.add(e.latitude);
            longitudes.add(e.longitude);
        }
    }

    @NonNull
    public String getName() { return name; }
    @NonNull
    public ArrayList<Double> getLatitudes(){return latitudes;}
    @NonNull
    public ArrayList<Double> getLongitudes(){return longitudes;}
/*
    public void setLatitudes(ArrayList<Double> latitudes){this.latitudes = latitudes;}

    public void setLongitudes(ArrayList<Double> longitudes){this.longitudes = longitudes;}
    */

    public ArrayList<LatLng> getEntrances() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < latitudes.size(); i++) {
            latLngs.add(new LatLng(latitudes.get(i), longitudes.get(i)));
        }
        return latLngs;
    }

    public static Location[] builtInLocations = new Location[]{
            new Location("Adelbert Hall", new LatLng[]{                // Quad
                    new LatLng(41.504916,-81.608155),
                    new LatLng(41.504683,-81.608451)}),
            new Location("Allen Ford", new LatLng[]{
                    new LatLng(41.505825, -81.608632)}),
            new Location("AW Smith", new LatLng[]{
                    new LatLng(41.503019, -81.607242),
                    new LatLng(41.502760, -81.606848)}),
            new Location("Bingham", new LatLng[]{
                    new LatLng(41.502547, -81.607081)}),
            new Location("Clapp", new LatLng[]{
                    new LatLng(41.504099, -81.606873),
                    new LatLng(41.503729, -81.607005)}),
            new Location("Crawford", new LatLng[]{
                    new LatLng(41.504481, -81.609677)}),
            new Location("DeGrace", new LatLng[]{
                    new LatLng(41.504213,-81.606968),
                    new LatLng(41.504107, -81.607204)}),
            new Location("Eldred", new LatLng[]{
                    new LatLng(41.504061, -81.607733),
                    new LatLng(41.503871, -81.607846)}),
            new Location("Glennan", new LatLng[]{
                    new LatLng(41.501769, -81.607189)}),
            new Location("Kent Hale Smith", new LatLng[]{
                    new LatLng(41.503436, -81.606430),
                    new LatLng(41.503201, -81.606726)}),
            new Location("Millis", new LatLng[]{
                    new LatLng(41.504099, -81.606873),
                    new LatLng(41.504501, -81.607790),
                    new LatLng(41.504296, -81.607496),
                    new LatLng(41.504059, -81.607658),
                    new LatLng(41.503853, -81.607278)}),
            new Location("Millis Schmitt", new LatLng[]{
                    new LatLng(41.504099, -81.606873),
                    new LatLng(41.503729, -81.607005)}),
            new Location("Morley", new LatLng[]{
                    new LatLng(41.503831, -81.607345)}),
            new Location("Nord", new LatLng[]{
                    new LatLng(41.502603, -81.607687),
                    new LatLng(41.502388, -81.607995)}),
            new Location("Olin", new LatLng[]{
                    new LatLng(41.502327,-81.607839)}),
            new Location("Rockefeller", new LatLng[]{
                    new LatLng(41.503580, -81.607958),
                    new LatLng(41.503687, -81.607651)}),
            new Location("Sears", new LatLng[]{
                    new LatLng(41.502871, -81.607966)}),
            new Location("Strosacker", new LatLng[]{
                    new LatLng(41.503236, -81.607529)}),
            new Location("Tomlinson", new LatLng[]{
                    new LatLng(41.504188, -81.609537)}),
            new Location("Veale", new LatLng[]{
                    new LatLng(41.501090, -81.606373)}),
            new Location("White", new LatLng[]{
                    new LatLng(41.502084, -81.607455)}),
            new Location("Yost", new LatLng[]{
                    new LatLng(41.503652, -81.608915),
                    new LatLng(41.503842, -81.609152),
                    new LatLng(41.503651, -81.608907)}),
            new Location("Church of the Covenant", new LatLng[]{
                    new LatLng(41.508254,-81.607256),
                    new LatLng(41.508343,-81.607515)}),
            new Location("Clark", new LatLng[]{                      // Mather Quad
                    new LatLng(41.509128, -81.607634),
                    new LatLng(41.508856,-81.607589)}),
            new Location("Guilford", new LatLng[]{
                    new LatLng(41.508739, -81.607634)}),
            new Location("Harkness Chapel", new LatLng[]{
                    new LatLng(41.509213, -81.607553)}),
            new Location("Haydn", new LatLng[]{
                    new LatLng(41.508688, -81.607733)}),
            new Location("Mather Dance Studio", new LatLng[]{
                    new LatLng(41.508276, -81.607972)}),
            new Location("Mather House", new LatLng[]{
                    new LatLng(41.507981, -81.607845)}),
            new Location("Peter B. Lewis", new LatLng[]{
                    new LatLng(41.510049, -81.607604),
                    new LatLng(41.509583, -81.608016)}),
            new Location("Tinkham Veale", new LatLng[]{
                    new LatLng(41.508757, -81.608493),
                    new LatLng(41.507596, -81.608756)}),
            new Location("Thwing", new LatLng[]{
                    new LatLng(41.507254, -81.608186),
                    new LatLng(41.507541, -81.608089),
                    new LatLng(41.507595, -81.608594),
                    new LatLng(41.507279, -81.608552)}),
            new Location("WRUW/Mather Memorial House", new LatLng[]{
                    new LatLng(41.509787, -81.607128)}),
            new Location("Coffee House", new LatLng[]{              // North Campus
                    new LatLng(41.511595,-81.607366)}),
            new Location("Den", new LatLng[]{
                    new LatLng(41.511964, -81.605974)}),
            new Location("Dively", new LatLng[]{
                    new LatLng(41.510112, -81.606889)}),
            new Location("Jack, Joseph, and Morton Mandel School of Applied Social Sciences", new LatLng[]{
                    new LatLng(41.510429, -81.607266)}),
            new Location("Kelvin-Smith Library", new LatLng[]{
                    new LatLng(41.507266, -81.609377)}),
            new Location("Linsalata", new LatLng[]{
                    new LatLng(41.511799, -81.607083)}),
            new Location("Mandel School of Applied Social Sciences", new LatLng[]{
                    new LatLng(41.511209,-81.605510)}),
            new Location("School of Law", new LatLng[]{
                    new LatLng(41.510382, -81.608730),
                    new LatLng(41.510323, -81.608671)}),
            new Location("Wolstein Hall", new LatLng[]{
                    new LatLng(41.510653, -81.605950)}),
            new Location("Clarke", new LatLng[]{                    // NRV
                    new LatLng(41.514455, -81.605709)}),
            new Location("Cutler", new LatLng[]{
                    new LatLng(41.514142, -81.605373)}),
            new Location("Denison", new LatLng[]{
                    new LatLng(41.513281, -81.604997)}),
            new Location("Hitchcock", new LatLng[]{
                    new LatLng(41.514016, -81.605126)}),
            new Location("Leutner", new LatLng[]{
                    new LatLng(41.513343, -81.605989)}),
            new Location("Norton", new LatLng[]{
                    new LatLng(41.512968, -81.605724)}),
            new Location("Pierce", new LatLng[]{
                    new LatLng(41.513913, -81.605606)}),
            new Location("Raymond", new LatLng[]{
                    new LatLng(41.512609, -81.605422)}),
            new Location("Smith", new LatLng[]{
                    new LatLng(41.512374, -81.607687)}),
            new Location("Sherman", new LatLng[]{
                    new LatLng(41.512708, -81.605578)}),
            new Location("Stephanie-Tubbs Jones", new LatLng[]{
                    new LatLng(41.515129,-81.605021)}),
            new Location("Storrs", new LatLng[]{
                    new LatLng(41.513969, -81.605884)}),
            new Location("Taft", new LatLng[]{
                    new LatLng(41.512756, -81.607186)}),
            new Location("Taplin", new LatLng[]{
                    new LatLng(41.512806, -81.607229)}),
            new Location("Tyler", new LatLng[]{
                    new LatLng(41.512859, -81.605995)}),
            new Location("Wade Commons", new LatLng[]{
                    new LatLng(41.513007, -81.605431),
                    new LatLng(41.512894, -81.605254)}),
            new Location("Alumni", new LatLng[]{                    // SRV
                    new LatLng(41.500547, -81.602553)}),
            new Location("Carlton Commons", new LatLng[]{
                    new LatLng(41.500387,-81.601833),
                    new LatLng(41.500259,-81.601569)}),
            new Location("Fribley", new LatLng[]{
                    new LatLng(41.501117, -81.602445)}),
            new Location("Glaser", new LatLng[]{
                    new LatLng(41.500531, -81.600689)}),
            new Location("Howe", new LatLng[]{
                    new LatLng(41.500831, -81.602256)}),
            new Location("Kusch", new LatLng[]{
                    new LatLng(41.500787, -81.600249)}),
            new Location("Michelson", new LatLng[]{
                    new LatLng(41.500375, -81.601199)}),
            new Location("Staley", new LatLng[]{
                    new LatLng(41.500080, -81.602771)}),
            new Location("Tippit", new LatLng[]{
                    new LatLng(41.500551, -81.602856)}),
            new Location("House 1", new LatLng[]{                   // Village
                    new LatLng(41.512559,-81.603321)}),
            new Location("House 2", new LatLng[]{
                    new LatLng(41.512128,-81.603345)}),
            new Location("House 3", new LatLng[]{
                    new LatLng(41.512485,-81.603873)}),
            new Location("House 3A", new LatLng[]{
                    new LatLng(41.512518,-81.604466)}),
            new Location("House 4", new LatLng[]{
                    new LatLng(41.512675,-81.604542)}),
            new Location("House 5", new LatLng[]{
                    new LatLng(41.513391,-81.604543)}),
            new Location("House 6", new LatLng[]{
                    new LatLng(41.514024,-81.604576)}),
            new Location("House 7", new LatLng[]{
                    new LatLng(41.514316,-81.604558)}),
            new Location("Starbucks (Village)", new LatLng[]{
                    new LatLng(41.512414,-81.604574)}),
            new Location("Wyant", new LatLng[]{
                    new LatLng(41.514356,-81.604584)}),
            new Location("Alpha Chi Omega", new LatLng[]{           // Greek
                    new LatLng(41.511725,-81.605330)}),
            new Location("Alpha Phi", new LatLng[]{
                    new LatLng(41.514273,-81.607836),
                    new LatLng(41.514090,-81.607882)}),
            new Location("Beta Theta Pi", new LatLng[]{
                    new LatLng(41.502396,-81.601743)}),
            new Location("Delta Gamma", new LatLng[]{
                    new LatLng(41.502136,-81.601578),
                    new LatLng(41.502252,-81.601502)}),
            new Location("Delta Kappa Epsilon", new LatLng[]{
                    new LatLng(41.501468,-81.600884)}),
            new Location("Delta Tau Delta", new LatLng[]{
                    new LatLng(41.513971,-81.607119),
                    new LatLng(41.513811,-81.607267)}),
            new Location("Delta Upsilon", new LatLng[]{
                    new LatLng(41.501417, -81.600642),
                    new LatLng(41.501300, -81.600389)}),
            new Location("Kappa Alpha Theta", new LatLng[]{
                    new LatLng(41.501758,-81.600338),
                    new LatLng(41.501636, -81.600105)}),
            new Location("Phi Delta Theta", new LatLng[]{
                    new LatLng(41.502865,-81.601342)}),
            new Location("Phi Gamma Delta (Fiji)", new LatLng[]{
                    new LatLng(41.510860,-81.606530)}),
            new Location("Phi Kappa Psi", new LatLng[]{
                    new LatLng(41.501468,-81.600884)}),
            new Location("Phi Kappa Tau", new LatLng[]{
                    new LatLng(41.501417, -81.600642),
                    new LatLng(41.501300, -81.600389)}),
            new Location("Phi Kappa Theta", new LatLng[]{
                    new LatLng(41.514319,-81.610007)}),
            new Location("Phi Mu", new LatLng[]{
                    new LatLng(41.511487,-81.605133)}),
            new Location("Phi Sigma Ro", new LatLng[]{
                    new LatLng(41.500958, -81.601090)}),
            new Location("Sigma Chi", new LatLng[]{
                    new LatLng(41.501417, -81.600642),
                    new LatLng(41.501300, -81.600389)}),
            new Location("Sigma Nu", new LatLng[]{
                    new LatLng(41.502596,-81.601259)}),
            new Location("Sigma Phi Epsilon", new LatLng[]{
                    new LatLng(41.502396,-81.601743)}),
            new Location("Sigma Psi", new LatLng[]{
                    new LatLng(41.501758,-81.600338),
                    new LatLng(41.501636, -81.600105)}),
            new Location("Theta Chi", new LatLng[]{
                    new LatLng(41.513653,-81.606821)}),
            new Location("Zeta Beta Tau", new LatLng[]{
                    new LatLng(41.514110,-81.607373),
                    new LatLng(41.513919,-81.6075569)}),
            new Location("Zeta Psi", new LatLng[]{
                    new LatLng(41.501890, -81.600506)}),
            new Location("ABC the Tavern", new LatLng[]{            // Uptown
                    new LatLng(41.509339,-81.603935)}),
            new Location("Barnes & Noble", new LatLng[]{
                    new LatLng(41.509962, -81.604270)}),
            new Location("Bibibop", new LatLng[]{
                    new LatLng(41.509577,-81.604733)}),
            new Location("Chapati", new LatLng[]{
                    new LatLng(41.509589,-81.604229)}),
            new Location("Chipotle", new LatLng[]{
                    new LatLng(41.509803,-81.603831)}),
            new Location("Chopstick", new LatLng[]{
                    new LatLng(41.508515,-81.605143)}),
            new Location("Constantino's", new LatLng[]{
                    new LatLng(41.510292,-81.603907)}),
            new Location("Dunkin' Donuts", new LatLng[]{
                    new LatLng(41.510229,-81.603970)}),
            new Location("Inchin's Bamboo Garden", new LatLng[]{
                    new LatLng(41.509683,-81.604156)}),
            new Location("Indian Flame", new LatLng[]{
                    new LatLng(41.511061,-81.602778)}),
            new Location("Falafel Cafe", new LatLng[]{
                    new LatLng(41.508815,-81.605683)}),
            new Location("Jimmy John's", new LatLng[]{
                    new LatLng(41.509703,-81.604084)}),
            new Location("Kung Fu Tea", new LatLng[]{
                    new LatLng(41.508400,-81.605724)}),
            new Location("Mitchell's", new LatLng[]{
                    new LatLng(41.509608,-81.603647)}),
            new Location("Otani Noodle", new LatLng[]{
                    new LatLng(41.500084,-81.603609)}),
            new Location("Panera Bread", new LatLng[]{
                    new LatLng(41.509803,-81.603831)}),
            new Location("Piccadilly", new LatLng[]{
                    new LatLng(41.510740,-81.603208)}),
            new Location("Potbelly", new LatLng[]{
                    new LatLng(41.509477,-81.604870)}),
            new Location("Qdoba", new LatLng[]{
                    new LatLng(41.508503,-81.605351)}),
            new Location("Rascal House", new LatLng[]{
                    new LatLng(41.508450,-81.605627)}),
            new Location("Simply Greek", new LatLng[]{
                    new LatLng(41.509829,-81.603386)}),
            new Location("Starbucks (Uptown)", new LatLng[]{
                    new LatLng(41.508167, -81.605999)}),
            new Location("Subway", new LatLng[]{
                    new LatLng(41.508572,-81.605984)}),
            new Location("Tropical Smoothie Cafe", new LatLng[]{
                    new LatLng(41.508484,-81.605529)})
    };


    public static Location[] populateData() {
        return builtInLocations;
    }

    public static String[] getLocationNames() {
        return Stream.of(builtInLocations).map(Location::getName).toArray(String[]::new);
    }

    public static Location getLocationByName(String name) {
        return Stream.of(builtInLocations).filter(l -> l.getName().equals(name))
                .findFirst().get();
    }

}
