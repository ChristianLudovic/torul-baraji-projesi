import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Location {
    private String name;
    private double latitude;
    private double longitude;
    private double depth;

    public Location(String name, double latitude, double longitude, double depth) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
    }

    public double[] getCoordinates() {
        return new double[]{latitude, longitude};
    }

    public void updateLocation(double latitude, double longitude, double depth) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
    }
}

class FishSample {
    private String species;
    private double length;
    private double weight;
    private String behavior;

    public FishSample(String species, double length, double weight, String behavior) {
        this.species = species;
        this.length = length;
        this.weight = weight;
        this.behavior = behavior;
    }

    public Map<String, Object> calculateMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("length_weight_ratio", weight != 0 ? length / weight : 0);
        metrics.put("size_category", length < 10 ? "small" : (length < 30 ? "medium" : "large"));
        return metrics;
    }

    public String classifySpecies() {
        return String.format("Classification pour %s basée sur L:%.2fcm, P:%.2fg", species, length, weight);
    }

    public String getSpecies() {
        return species;
    }
}

class SoundRecording {
    private LocalDateTime timestamp;
    private String fileFormat;
    private int duration;
    private boolean analyzed;

    public SoundRecording(LocalDateTime timestamp, String fileFormat, int duration) {
        this.timestamp = timestamp;
        this.fileFormat = fileFormat;
        this.duration = duration;
        this.analyzed = false;
    }

    public Map<String, Object> analyzeSound() {
        analyzed = true;
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("duration", duration);
        analysis.put("format", fileFormat);
        return analysis;
    }

    public String exportData() {
        return String.format("Enregistrement du %s - Durée: %ds", timestamp, duration);
    }
}

class TrapData {
    private String trapType;
    private LocalDateTime setTime;
    private LocalDateTime checkTime;
    private int catchCount;

    public TrapData(String trapType, LocalDateTime setTime) {
        this.trapType = trapType;
        this.setTime = setTime;
        this.catchCount = 0;
    }

    public void recordCatch(int count, LocalDateTime checkTime) {
        this.catchCount = count;
        this.checkTime = checkTime;
    }

    public double calculateEfficiency() {
        if (checkTime == null) return 0;
        double hoursDeployed = java.time.Duration.between(setTime, checkTime).toHours();
        return hoursDeployed > 0 ? catchCount / hoursDeployed : 0;
    }
}

class Observation {
    private LocalDateTime date;
    private String weather;
    private double waterTemp;
    private String observer;
    private Location location;
    private List<FishSample> fishSamples;
    private List<SoundRecording> soundRecordings;
    private TrapData trapData;

    public Observation(LocalDateTime date, String weather, double waterTemp, String observer, Location location) {
        this.date = date;
        this.weather = weather;
        this.waterTemp = waterTemp;
        this.observer = observer;
        this.location = location;
        this.fishSamples = new ArrayList<>();
        this.soundRecordings = new ArrayList<>();
    }

    public void recordData(FishSample fishSample) {
        fishSamples.add(fishSample);
    }

    public void attachPhotos(List<String> photoPaths) {
        System.out.println("Photos attachées: " + String.join(", ", photoPaths));
    }

    public void addSoundRecording(SoundRecording recording) {
        soundRecordings.add(recording);
    }

    public List<FishSample> getFishSamples() {
        return fishSamples;
    }

    public List<SoundRecording> getSoundRecordings() {
        return soundRecordings;
    }
}

class FishStudy {
    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Observation> observations;

    public FishStudy(String projectName, LocalDateTime startDate, LocalDateTime endDate) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.observations = new ArrayList<>();
    }

    public void addObservation(Observation observation) {
        observations.add(observation);
    }

    public String generateReport() {
        int totalSamples = observations.stream()
            .mapToInt(obs -> obs.getFishSamples().size())
            .sum();
        int totalRecordings = observations.stream()
            .mapToInt(obs -> obs.getSoundRecordings().size())
            .sum();

        return String.format("""
            Rapport d'étude: %s
            Période: %s - %s
            Nombre total d'observations: %d
            Nombre total d'échantillons: %d
            Nombre total d'enregistrements: %d
            """,
            projectName,
            startDate.toLocalDate(),
            endDate.toLocalDate(),
            observations.size(),
            totalSamples,
            totalRecordings
        );
    }

    public Map<String, Object> analyzeTrends() {
        Map<String, Integer> speciesCount = new HashMap<>();
        
        for (Observation obs : observations) {
            for (FishSample sample : obs.getFishSamples()) {
                speciesCount.merge(sample.getSpecies(), 1, Integer::sum);
            }
        }

        Map<String, Object> trends = new HashMap<>();
        trends.put("species_distribution", speciesCount);
        trends.put("observation_count", observations.size());
        return trends;
    }
}

public class Main {
    public static void main(String[] args) {
        // Création d'une nouvelle étude
        FishStudy study = new FishStudy(
            "Étude Barrage Torul",
            LocalDateTime.of(2024, 1, 1, 0, 0),
            LocalDateTime.of(2024, 12, 31, 23, 59)
        );

        // Création d'une location
        Location location = new Location("Point A", 40.5678, 39.8765, 15.5);

        // Création d'une observation
        Observation observation = new Observation(
            LocalDateTime.now(),
            "Ensoleillé",
            18.5,
            "Dr. Smith",
            location
        );

        // Ajout d'un échantillon de poisson
        FishSample fish = new FishSample("Truite", 25.5, 500, "Nage active");
        observation.recordData(fish);

        // Ajout d'un enregistrement sonore
        SoundRecording sound = new SoundRecording(LocalDateTime.now(), "WAV", 120);
        observation.addSoundRecording(sound);

        // Ajout de l'observation à l'étude
        study.addObservation(observation);

        // Génération d'un rapport
        System.out.println(study.generateReport());
    }
}