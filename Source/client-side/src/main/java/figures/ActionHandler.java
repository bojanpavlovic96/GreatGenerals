package figures;

import board.FieldArgs;

public interface ActionHandler {

	Figure react(Figure first_figure, Figure second_figure, FieldArgs field_args);

}
