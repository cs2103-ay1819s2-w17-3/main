package seedu.finance.ui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.finance.commons.core.LogsCenter;
import seedu.finance.model.record.Record;

/**
 * Panel containing the list of records.
 */
public class RecordListPanel extends UiPart<Region> {
    private static final String FXML = "RecordListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(RecordListPanel.class);

    @FXML
    private ListView<Record> recordListView;

    public RecordListPanel(ObservableList<Record> recordList, ObservableValue<Record> selectedRecord,
                           Consumer<Record> onSelectedRecordChange) {
        super(FXML);
        recordListView.setItems(recordList);
        recordListView.setCellFactory(listView -> new RecordListViewCell());
        recordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.fine("Selection in record list panel changed to : '" + newValue + "'");
            onSelectedRecordChange.accept(newValue);
        });
        selectedRecord.addListener((observable, oldValue, newValue) -> {
            logger.fine("Selected record changed to: " + newValue);

            // Don't modify selection if we are already selecting the selected record,
            // otherwise we would have an infinite loop.
            if (Objects.equals(recordListView.getSelectionModel().getSelectedItem(), newValue)) {
                return;
            }

            if (newValue == null) {
                recordListView.getSelectionModel().clearSelection();
            } else {
                int index = recordListView.getItems().indexOf(newValue);
                recordListView.scrollTo(index);
                recordListView.getSelectionModel().clearAndSelect(index);
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Record} using a {@code RecordCard}.
     */
    class RecordListViewCell extends ListCell<Record> {
        @Override
        protected void updateItem(Record record, boolean empty) {
            super.updateItem(record, empty);

            if (empty || record == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new RecordCard(record, getIndex() + 1).getRoot());
            }
        }
    }

}
