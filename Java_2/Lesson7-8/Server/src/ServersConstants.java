public interface ServersConstants {
    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 400;

    final String TITLE = "Сетевой чат (Сервер)";

    final int SERVER_PORT = 2048; // servet port
    final String SERVER_START = "Сервер начал работу...";
    final String SERVER_STOP = "Сервер завершил работу";
    final String CLIENT_JOINED = " клиент подключился.";
    final String CLIENT_DISCONNECTED = " отключился.";
    final String PVT_MSG = "/w";
    final String AUTH_FAIL = "Авторизация не пройдена";
    final String SQL_SELECT = "SELECT * FROM users WHERE login = '?'";
    final String EMPTY_NAME = "Server: There is no client with this name";
    final String EXIT_COMMAND = "exit"; // command for exit
    final String DATE_FORMAT = "yyyy.MM.dd hh:mm:ss";
    final String LAPS_FILE_NAME = "login_and_password.dat";
    final String ANONIM = "Anonim";
    final String NAME_IS_ALREADY_AUTORIZED = "Клиент с таким именем уже авторизирован";
    final String COMMAND = "/command";
    final String UPDATE_LIST_COMMAND = "updateList";
    final String UNKNOWN_COMMAND = "Неизвестная команда";
    final String BEGIN_OF_NAME = "BEGIN_OF_NAME";
    final String END_OF_NAME = "END_OF_NAME";
}
