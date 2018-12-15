package edu.cwru.students.cwrumapper.user.Archived;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Building {

    private String name;

    // latitudes and longitudes of all entrances to this building
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    public Building(String n, ArrayList<Double> latitudes, ArrayList<Double> longitudes){
        this.name = n;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
    }

    public Building(String n, LatLng[] lls) {
        name = n;

        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        for (int i = 0; i < lls.length; i++) {
            LatLng e = lls[i];
            latitudes.add(e.latitude);
            longitudes.add(e.longitude);
        }
    }

    public String getName() { return name; }

    public ArrayList<LatLng> getEntrances() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < latitudes.size(); i++) {
            latLngs.add(new LatLng(latitudes.get(i), longitudes.get(i)));
        }
        return latLngs;
    }

    public static Building[] populateData() {
        return new Building[]{
                new Building("Adelbert Hall", new LatLng[]{                // Quad
                        new LatLng(41.504916,-81.608155),
                        new LatLng(41.504683,-81.608451)}),
                new Building("Allen Ford", new LatLng[]{
                        new LatLng(41.505825, -81.608632)}),
                new Building("AW Smith", new LatLng[]{
                        new LatLng(41.503019, -81.607242),
                        new LatLng(41.502760, -81.606848)}),
                new Building("Bingham", new LatLng[]{
                        new LatLng(41.502547, -81.607081)}),
                new Building("Clapp", new LatLng[]{
                        new LatLng(41.504099, -81.606873),
                        new LatLng(41.503729, -81.607005)}),
                new Building("Crawford", new LatLng[]{
                        new LatLng(41.504481, -81.609677)}),
                new Building("DeGrace", new LatLng[]{
                        new LatLng(41.504213,-81.606968),
                        new LatLng(41.504107, -81.607204)}),
                new Building("Eldred", new LatLng[]{
                        new LatLng(41.504061, -81.607733),
                        new LatLng(41.503871, -81.607846)}),
                new Building("Glennan", new LatLng[]{
                        new LatLng(41.501769, -81.607189)}),
                new Building("Kent Hale Smith", new LatLng[]{
                        new LatLng(41.503436, -81.606430),
                        new LatLng(41.503201, -81.606726)}),
                new Building("Millis", new LatLng[]{
                        new LatLng(41.504099, -81.606873),
                        new LatLng(41.504501, -81.607790),
                        new LatLng(41.504296, -81.607496),
                        new LatLng(41.504059, -81.607658),
                        new LatLng(41.503853, -81.607278)}),
                new Building("Millis Schmitt", new LatLng[]{
                        new LatLng(41.504099, -81.606873),
                        new LatLng(41.503729, -81.607005)}),
                new Building("Morley", new LatLng[]{
                        new LatLng(41.503831, -81.607345)}),
                new Building("Nord", new LatLng[]{
                        new LatLng(41.502603, -81.607687),
                        new LatLng(41.502388, -81.607995)}),
                new Building("Olin", new LatLng[]{
                        new LatLng(41.502327,-81.607839)}),
                new Building("Rockefeller", new LatLng[]{
                        new LatLng(41.503580, -81.607958),
                        new LatLng(41.503687, -81.607651)}),
                new Building("Sears", new LatLng[]{
                        new LatLng(41.502871, -81.607966)}),
                new Building("Strosacker", new LatLng[]{
                        new LatLng(41.503236, -81.607529)}),
                new Building("Tomlinson", new LatLng[]{
                        new LatLng(41.504188, -81.609537)}),
                new Building("Veale", new LatLng[]{
                        new LatLng(41.501090, -81.606373)}),
                new Building("White", new LatLng[]{
                        new LatLng(41.502084, -81.607455)}),
                new Building("Yost", new LatLng[]{
                        new LatLng(41.503652, -81.608915),
                        new LatLng(41.503842, -81.609152),
                        new LatLng(41.503651, -81.608907)}),
                new Building("Church of the Covenant", new LatLng[]{
                        new LatLng(41.508254,-81.607256),
                        new LatLng(41.508343,-81.607515)}),
                new Building("Clark", new LatLng[]{                      // Mather Quad
                        new LatLng(41.509128, -81.607634),
                        new LatLng(41.508856,-81.607589)}),
                new Building("Guilford", new LatLng[]{
                        new LatLng(41.508739, -81.607634)}),
                new Building("Harkness Chapel", new LatLng[]{
                        new LatLng(41.509213, -81.607553)}),
                new Building("Haydn", new LatLng[]{
                        new LatLng(41.508688, -81.607733)}),
                new Building("Mather Dance Studio", new LatLng[]{
                        new LatLng(41.508276, -81.607972)}),
                new Building("Mather House", new LatLng[]{
                        new LatLng(41.507981, -81.607845)}),
                new Building("Peter B. Lewis", new LatLng[]{
                        new LatLng(41.510049, -81.607604),
                        new LatLng(41.509583, -81.608016)}),
                new Building("Tinkham Veale", new LatLng[]{
                        new LatLng(41.508757, -81.608493),
                        new LatLng(41.507596, -81.608756)}),
                new Building("Thwing", new LatLng[]{
                        new LatLng(41.507254, -81.608186),
                        new LatLng(41.507541, -81.608089),
                        new LatLng(41.507595, -81.608594),
                        new LatLng(41.507279, -81.608552)}),
                new Building("WRUW/Mather Memorial House", new LatLng[]{
                        new LatLng(41.509787, -81.607128)}),
                new Building("Coffee House", new LatLng[]{              // North Campus
                        new LatLng(41.511595,-81.607366)}),
                new Building("Den", new LatLng[]{
                        new LatLng(41.511964, -81.605974)}),
                new Building("Dively", new LatLng[]{
                        new LatLng(41.510112, -81.606889)}),
                new Building("Jack, Joseph, and Morton Mandel School of Applied Social Sciences", new LatLng[]{
                        new LatLng(41.510429, -81.607266)}),
                new Building("Kelvin-Smith Library", new LatLng[]{
                        new LatLng(41.507266, -81.609377)}),
                new Building("Linsalata", new LatLng[]{
                        new LatLng(41.511799, -81.607083)}),
                new Building("Mandel School of Applied Social Sciences", new LatLng[]{
                        new LatLng(41.511209,-81.605510)}),
                new Building("School of Law", new LatLng[]{
                        new LatLng(41.510382, -81.608730),
                        new LatLng(41.510323, -81.608671)}),
                new Building("Wolstein Hall", new LatLng[]{
                        new LatLng(41.510653, -81.605950)}),
                new Building("Clarke", new LatLng[]{                    // NRV
                        new LatLng(41.514455, -81.605709)}),
                new Building("Cutler", new LatLng[]{
                        new LatLng(41.514142, -81.605373)}),
                new Building("Denison", new LatLng[]{
                        new LatLng(41.513281, -81.604997)}),
                new Building("Hitchcock", new LatLng[]{
                        new LatLng(41.514016, -81.605126)}),
                new Building("Leutner", new LatLng[]{
                        new LatLng(41.513343, -81.605989)}),
                new Building("Norton", new LatLng[]{
                        new LatLng(41.512968, -81.605724)}),
                new Building("Pierce", new LatLng[]{
                        new LatLng(41.513913, -81.605606)}),
                new Building("Raymond", new LatLng[]{
                        new LatLng(41.512609, -81.605422)}),
                new Building("Smith", new LatLng[]{
                        new LatLng(41.512374, -81.607687)}),
                new Building("Sherman", new LatLng[]{
                        new LatLng(41.512708, -81.605578)}),
                new Building("Stephanie-Tubbs Jones", new LatLng[]{
                        new LatLng(41.515129,-81.605021)}),
                new Building("Storrs", new LatLng[]{
                        new LatLng(41.513969, -81.605884)}),
                new Building("Taft", new LatLng[]{
                        new LatLng(41.512756, -81.607186)}),
                new Building("Taplin", new LatLng[]{
                        new LatLng(41.512806, -81.607229)}),
                new Building("Tyler", new LatLng[]{
                        new LatLng(41.512859, -81.605995)}),
                new Building("Wade Commons", new LatLng[]{
                        new LatLng(41.513007, -81.605431),
                        new LatLng(41.512894, -81.605254)}),
                new Building("Alumni", new LatLng[]{                    // SRV
                        new LatLng(41.500547, -81.602553)}),
                new Building("Carlton Commons", new LatLng[]{
                        new LatLng(41.500387,-81.601833),
                        new LatLng(41.500259,-81.601569)}),
                new Building("Fribley", new LatLng[]{
                        new LatLng(41.501117, -81.602445)}),
                new Building("Glaser", new LatLng[]{
                        new LatLng(41.500531, -81.600689)}),
                new Building("Howe", new LatLng[]{
                        new LatLng(41.500831, -81.602256)}),
                new Building("Kusch", new LatLng[]{
                        new LatLng(41.500787, -81.600249)}),
                new Building("Michelson", new LatLng[]{
                        new LatLng(41.500375, -81.601199)}),
                new Building("Staley", new LatLng[]{
                        new LatLng(41.500080, -81.602771)}),
                new Building("Tippit", new LatLng[]{
                        new LatLng(41.500551, -81.602856)}),
                new Building("House 1", new LatLng[]{                   // Village
                        new LatLng(41.512559,-81.603321)}),
                new Building("House 2", new LatLng[]{
                        new LatLng(41.512128,-81.603345)}),
                new Building("House 3", new LatLng[]{
                        new LatLng(41.512485,-81.603873)}),
                new Building("House 3A", new LatLng[]{
                        new LatLng(41.512518,-81.604466)}),
                new Building("House 4", new LatLng[]{
                        new LatLng(41.512675,-81.604542)}),
                new Building("House 5", new LatLng[]{
                        new LatLng(41.513391,-81.604543)}),
                new Building("House 6", new LatLng[]{
                        new LatLng(41.514024,-81.604576)}),
                new Building("House 7", new LatLng[]{
                        new LatLng(41.514316,-81.604558)}),
                new Building("Starbucks (Village)", new LatLng[]{
                        new LatLng(41.512414,-81.604574)}),
                new Building("Wyant", new LatLng[]{
                        new LatLng(41.514356,-81.604584)}),
                new Building("Alpha Chi Omega", new LatLng[]{           // Greek
                        new LatLng(41.511725,-81.605330)}),
                new Building("Alpha Phi", new LatLng[]{
                        new LatLng(41.514273,-81.607836),
                        new LatLng(41.514090,-81.607882)}),
                new Building("Beta Theta Pi", new LatLng[]{
                        new LatLng(41.502396,-81.601743)}),
                new Building("Delta Gamma", new LatLng[]{
                        new LatLng(41.502136,-81.601578),
                        new LatLng(41.502252,-81.601502)}),
                new Building("Delta Kappa Epsilon", new LatLng[]{
                        new LatLng(41.501468,-81.600884)}),
                new Building("Delta Tau Delta", new LatLng[]{
                        new LatLng(41.513971,-81.607119),
                        new LatLng(41.513811,-81.607267)}),
                new Building("Delta Upsilon", new LatLng[]{
                        new LatLng(41.501417, -81.600642),
                        new LatLng(41.501300, -81.600389)}),
                new Building("Kappa Alpha Theta", new LatLng[]{
                        new LatLng(41.501758,-81.600338),
                        new LatLng(41.501636, -81.600105)}),
                new Building("Phi Delta Theta", new LatLng[]{
                        new LatLng(41.502865,-81.601342)}),
                new Building("Phi Gamma Delta (Fiji)", new LatLng[]{
                        new LatLng(41.510860,-81.606530)}),
                new Building("Phi Kappa Psi", new LatLng[]{
                        new LatLng(41.501468,-81.600884)}),
                new Building("Phi Kappa Tau", new LatLng[]{
                        new LatLng(41.501417, -81.600642),
                        new LatLng(41.501300, -81.600389)}),
                new Building("Phi Kappa Theta", new LatLng[]{
                        new LatLng(41.514319,-81.610007)}),
                new Building("Phi Mu", new LatLng[]{
                        new LatLng(41.511487,-81.605133)}),
                new Building("Phi Sigma Ro", new LatLng[]{
                        new LatLng(41.500958, -81.601090)}),
                new Building("Sigma Chi", new LatLng[]{
                        new LatLng(41.501417, -81.600642),
                        new LatLng(41.501300, -81.600389)}),
                new Building("Sigma Nu", new LatLng[]{
                        new LatLng(41.502596,-81.601259)}),
                new Building("Sigma Phi Epsilon", new LatLng[]{
                        new LatLng(41.502396,-81.601743)}),
                new Building("Sigma Psi", new LatLng[]{
                        new LatLng(41.501758,-81.600338),
                        new LatLng(41.501636, -81.600105)}),
                new Building("Theta Chi", new LatLng[]{
                        new LatLng(41.513653,-81.606821)}),
                new Building("Zeta Beta Tau", new LatLng[]{
                        new LatLng(41.514110,-81.607373),
                        new LatLng(41.513919,-81.6075569)}),
                new Building("Zeta Psi", new LatLng[]{
                        new LatLng(41.501890, -81.600506)}),
                new Building("ABC the Tavern", new LatLng[]{            // Uptown
                        new LatLng(41.509339,-81.603935)}),
                new Building("Barnes & Noble", new LatLng[]{
                        new LatLng(41.509962, -81.604270)}),
                new Building("Bibibop", new LatLng[]{
                        new LatLng(41.509577,-81.604733)}),
                new Building("Chapati", new LatLng[]{
                        new LatLng(41.509589,-81.604229)}),
                new Building("Chipotle", new LatLng[]{
                        new LatLng(41.509803,-81.603831)}),
                new Building("Chopstick", new LatLng[]{
                        new LatLng(41.508515,-81.605143)}),
                new Building("Constantino's", new LatLng[]{
                        new LatLng(41.510292,-81.603907)}),
                new Building("Dunkin' Donuts", new LatLng[]{
                        new LatLng(41.510229,-81.603970)}),
                new Building("Inchin's Bamboo Garden", new LatLng[]{
                        new LatLng(41.509683,-81.604156)}),
                new Building("Indian Flame", new LatLng[]{
                        new LatLng(41.511061,-81.602778)}),
                new Building("Falafel Cafe", new LatLng[]{
                        new LatLng(41.508815,-81.605683)}),
                new Building("Jimmy John's", new LatLng[]{
                        new LatLng(41.509703,-81.604084)}),
                new Building("Kung Fu Tea", new LatLng[]{
                        new LatLng(41.508400,-81.605724)}),
                new Building("Mitchell's", new LatLng[]{
                        new LatLng(41.509608,-81.603647)}),
                new Building("Otani Noodle", new LatLng[]{
                        new LatLng(41.500084,-81.603609)}),
                new Building("Panera Bread", new LatLng[]{
                        new LatLng(41.509803,-81.603831)}),
                new Building("Piccadilly", new LatLng[]{
                        new LatLng(41.510740,-81.603208)}),
                new Building("Potbelly", new LatLng[]{
                        new LatLng(41.509477,-81.604870)}),
                new Building("Qdoba", new LatLng[]{
                        new LatLng(41.508503,-81.605351)}),
                new Building("Rascal House", new LatLng[]{
                        new LatLng(41.508450,-81.605627)}),
                new Building("Simply Greek", new LatLng[]{
                        new LatLng(41.509829,-81.603386)}),
                new Building("Starbucks (Uptown)", new LatLng[]{
                        new LatLng(41.508167, -81.605999)}),
                new Building("Subway", new LatLng[]{
                        new LatLng(41.508572,-81.605984)}),
                new Building("Tropical Smoothie Cafe", new LatLng[]{
                        new LatLng(41.508484,-81.605529)})
        };
    }
}
