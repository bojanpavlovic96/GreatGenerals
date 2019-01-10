package database.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "game")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private Board board;

	@ManyToMany
	private List<Player> players;

	@ManyToOne
	private Player winner;

	private Date date_time;

	@OneToMany
	private List<Chat> chats;

	@OneToMany
	private List<Move> moves;

	public Game() {

	}

	public Game(long id, Board board, List<Player> players, Player winner, Date date_time,
			List<Chat> chats, List<Move> moves) {
		super();
		this.id = id;
		this.board = board;
		this.players = players;
		this.winner = winner;
		this.date_time = date_time;
		this.chats = chats;
		this.moves = moves;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
