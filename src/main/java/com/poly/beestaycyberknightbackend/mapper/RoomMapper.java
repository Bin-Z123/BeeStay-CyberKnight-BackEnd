        package com.poly.beestaycyberknightbackend.mapper;

    import com.poly.beestaycyberknightbackend.dto.request.RoomUpdateRequest;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.MappingTarget;
    import org.mapstruct.factory.Mappers;

    import com.poly.beestaycyberknightbackend.domain.Room;
    import com.poly.beestaycyberknightbackend.dto.request.RoomRequest;
    import com.poly.beestaycyberknightbackend.dto.response.RoomResponse;

    @Mapper(componentModel = "spring")
    public interface RoomMapper {
        RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

        
        RoomResponse toRoomResponse(Room room); 
        Room toRoom(RoomRequest roomreq);
        void updateRoom(RoomUpdateRequest req, @MappingTarget Room room);
    }
