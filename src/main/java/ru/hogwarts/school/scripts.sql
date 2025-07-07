select *
from student as s
where s.age >= 10
  and s.age <= 20;


select s.name
from student as s;


select *
from student as s
where s.name like '%О%';


select *
from student as s
where s.age < s.id;


select *
from student as s
order by s.age;