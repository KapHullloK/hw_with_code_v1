alter table student
    add constraint valid_age_student check ( age >= 16 );


alter table student
    alter column name set not null;


alter table student
    add constraint unique_name_student unique (name);


alter table faculty
    add constraint unique_name_and_color_faculty unique (name, color);


alter table student
    alter column age set default 20;

