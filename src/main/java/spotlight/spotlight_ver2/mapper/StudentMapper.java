package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.StudentDTO;
import spotlight.spotlight_ver2.entity.Student;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDTO toDTO(Student student);
    Student toEntity(StudentDTO studentDTO);
}
