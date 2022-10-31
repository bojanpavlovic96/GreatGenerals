package controller.command;

import java.util.List;
import java.util.stream.Collectors;

import model.PlayerModelData;
import model.event.AttackModelEventArg;
import model.event.MoveModelEventArg;
import root.command.Command;
import root.communication.MessageInterpreter;
import root.communication.PlayerDescription;
import root.communication.messages.AbortMoveMsg;
import root.communication.messages.AttackMsg;
import root.communication.messages.InitializeMsg;
import root.communication.messages.Message;
import root.communication.messages.MoveMsg;
import root.communication.messages.ReadyForInitMsg;
import root.communication.messages.RecalculatePathMsg;
import root.communication.messages.ServerErrorMsg;
import root.model.PlayerData;
import root.model.event.ModelEventArg;

public class SwitchCaseMsgInterpreter implements MessageInterpreter {

    @Override
    public Command ToCommand(Message message) {
        switch (message.type) {
            case CreateRoomRequest:
            case RoomResponse:
            case JoinRoomRequest:
            case LeaveRoomRequest:
            case StartGameRequest:
                System.out.println("SwitchCaseMsgInterpretor intended for game "
                        + "related messages translation received room/login related message ... ");
                System.out.println(message.type.toString());

                break;
            // Above types are used in roomServer and can not be translated
            // to commands. TODO Consider spliting message types to login, room and game 
            // related messages. 

            case InitializeMessage:
                return new CtrlInitializeCommand(
                        mapPlayers(((InitializeMsg) message).players),
                        ((InitializeMsg) message).fields);

            case MoveMessage:
                return new CtrlMoveCommand(
                        ((MoveMsg) message).startFieldPos,
                        ((MoveMsg) message).endFieldPos);

            case AttackMessage:
                return new CtrlAttackCommand(
                        ((AttackMsg) message).startFieldPos,
                        ((AttackMsg) message).endFieldPos);

            case RecalculatePathMessage:
                return new CtrlRecalculatePathCommand(
                        ((RecalculatePathMsg) message).unitPosition);

            case AbortMoveMessage:
                return new CtrlAbortMoveCommand(
                        ((AbortMoveMsg) message).unitPosition);

            case ServerErrorMessage:
                return new CtrlServerErrorMessage(
                        ((ServerErrorMsg) message).message);

            default:
                break;

        }

        return null;
    }

    // region initMsg mapping

    private List<PlayerData> mapPlayers(List<PlayerDescription> players) {
        return players.stream()
                .map((p) -> mapPlayer(p))
                .collect(Collectors.toList());
    }

    private PlayerModelData mapPlayer(PlayerDescription player) {
        return new PlayerModelData(player.getUsername(),
                player.getColor(),
                player.getLevel(),
                player.getPoints());
    }

    // private List<FieldDesc> mapFields(List<Field> fields) {
    //     return fields.stream()
    //             .map((f) -> mapField(f))
    //             .collect(Collectors.toList());
    // }

    // private CtrlInitializeCommand.FieldDesc mapField(Field field) {
    //     return new CtrlInitializeCommand.FieldDesc(
    //             field.isVisible,
    //             field.position,
    //             field.owner,
    //             mapUnit(field.unit),
    //             mapTerrain(field.terrain));
    // }

    // private CtrlInitializeCommand.UnitDesc mapUnit(Unit unit) {
    //     return new CtrlInitializeCommand.UnitDesc(
    //             unit.unitName,
    //             unit.moveType,
    //             unit.groundAttackType,
    //             unit.airAttackType);
    // }

    // private CtrlInitializeCommand.TerrainDesc mapTerrain(Terrain terrain) {
    //     return new CtrlInitializeCommand.TerrainDesc(
    //             terrain.name,
    //             terrain.intensity);
    // }

    // endregion

    @Override
    public Message ToMessage(ModelEventArg eventArg) {
        switch (eventArg.getEventType()) {
            case Attack:
                return new AttackMsg(
                        ((AttackModelEventArg) eventArg).getSourceField(),
                        ((AttackModelEventArg) eventArg).getDestinationField());

            case Move:
                return new MoveMsg(
                        ((MoveModelEventArg) eventArg).getSourceField(),
                        ((MoveModelEventArg) eventArg).getDestinationField());

            case ReadyForInit:
                // username and roomName are already present in the Message super-class
                // and they are set with .setOrigin method inside the server proxy 
                return new ReadyForInitMsg();

            default:
                break;

        }
        return null;
    }

}