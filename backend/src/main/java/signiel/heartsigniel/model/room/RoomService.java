package signiel.heartsigniel.model.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.user.UserService;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepo roomRepo;
    private final UserService userService;
    private final RoomService roomService;



}
