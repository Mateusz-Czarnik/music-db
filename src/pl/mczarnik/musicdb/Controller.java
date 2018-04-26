package pl.mczarnik.musicdb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import pl.mczarnik.musicdb.model.Album;
import pl.mczarnik.musicdb.model.Artist;
import pl.mczarnik.musicdb.model.Datasource;

public class Controller {

    @FXML
    private TableView artistTable;

    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        artistTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    public void listAlbumsForArtist() {
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        if (artist == null) {
            System.out.println("No artist selected!");
        }

        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            @Override
            protected ObservableList<Album> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().queryAlbumsForArtistId(artist.getId()));
            }
        };
        artistTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }
}

// Implement as class beceuse may be used in multiple places
class GetAllArtistsTask extends Task {
    // Creating observable list from artists list returned
    @Override
    public ObservableList<Artist> call() {
        return FXCollections.observableArrayList(Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
    }
}
