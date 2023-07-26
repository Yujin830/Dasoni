package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    public Room createRoom( ) {
        Room room = new Room();
        return room;
    }

}
