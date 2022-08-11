package root.command;

// tried to replace string Command.name with the enum but ...
// ctrl and view commands both extend single class
public enum CommandType {
	Initialize,
	Move,
	ClearField,
	ClearMenu,
	ClearTopLayer,
	ClearView,
	ComplexSelectField,
	ComplexUnselectField,
	DrawField,
	LoadBoard,
	PopulateMenu,
	SelectField,
	SelectUnitPath,
	ShowFieldMenu,
	UnselectField,
	UnselectUnitPath,
	ZoomIn,
	ZoomOut
}
