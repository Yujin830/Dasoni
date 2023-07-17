insert into room values (1, 0, now(), 6, 1, "방제목" , 0, 1);
insert into room values (2, 1, now(), 3, 1, "방제목3" , 1, 1);

insert into room_q values(rand() * (select count(*) from question),1); 

select *
from room_q;
