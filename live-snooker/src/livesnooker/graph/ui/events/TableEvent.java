package livesnooker.graph.ui.events;

import livesnooker.graph.model.Table;

public class TableEvent {
	private Table table;

	public TableEvent(Table src) {
		this.table = src;
	}

	public Table getTable() {
		return table;
	}
}
