SELECT s.name, s.age, f.name FROM student s LEFT JOIN faculty f on f.id = s.faculty_id;
SELECT s.name, s.age FROM avatar a INNER JOIN student s on s.id = a.student_id;