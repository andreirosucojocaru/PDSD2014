package ro.pub.systems.pdsd.ex3coursesteachers;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	Activity context;
	Database database;
	
	class CourseButtonListener implements View.OnClickListener {
		
		private ArrayAdapter<Course> courseArrayAdapter;
		
		public CourseButtonListener(ArrayAdapter<Course> courseArrayAdapter) {
			this.courseArrayAdapter = courseArrayAdapter;
		}
		
		@Override
		public void onClick(View button1) {
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(R.layout.dialog, null);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			final AlertDialog dialog = builder.create();
			dialog.show();
			final EditText nameEditText = (EditText)view.findViewById(R.id.nameEditText);
			Button addButton = (Button)view.findViewById(R.id.addButton);
			
			addButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View button2) {
					Course course = new Course(database.generateNextId(Database.COURSES_TABLE_NAME, Database.COURSE_ID), nameEditText.getText().toString());
					database.addCourse(course);
					courseArrayAdapter.add(course);
					dialog.dismiss();
				}
			});
		}		
	}
	
	class TeacherButtonListener implements View.OnClickListener {	
		
		private ArrayAdapter<Teacher> teacherArrayAdapter;
		
		public TeacherButtonListener(ArrayAdapter<Teacher> teacherArrayAdapter) {
			this.teacherArrayAdapter = teacherArrayAdapter;
		}		
		
		public void onClick(View button1) {
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(R.layout.dialog, null);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			final AlertDialog dialog = builder.create();
			dialog.show();
			final EditText nameEditText = (EditText)view.findViewById(R.id.nameEditText);
			Button addButton = (Button)view.findViewById(R.id.addButton);
			addButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View button2) {
					Teacher teacher = new Teacher(database.generateNextId(Database.TEACHERS_TABLE_NAME, Database.TEACHER_ID), nameEditText.getText().toString());
					database.addTeacher(teacher);
					teacherArrayAdapter.add(teacher);
					dialog.dismiss();
				}
			});
		}		
	}
	
	class AssociationCourseTeacherButtonListener implements View.OnClickListener {		
		@Override
		public void onClick(View button1) {
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(R.layout.association_dialog, null);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			final AlertDialog dialog = builder.create();
			dialog.show();
			
			final Spinner courseSpinner = (Spinner)view.findViewById(R.id.courseSpinner);
			ArrayAdapter<Course> courseArrayAdapter = new ArrayAdapter<Course>(context, android.R.layout.simple_spinner_item, database.selectCourses(-1));
			courseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
			courseSpinner.setAdapter(courseArrayAdapter);
			
			final Spinner teacherSpinner = (Spinner)view.findViewById(R.id.teacherSpinner);
			ArrayAdapter<Teacher> teacherArrayAdapter = new ArrayAdapter<Teacher>(context, android.R.layout.simple_spinner_item, database.selectTeachers(-1));
			teacherArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			teacherSpinner.setAdapter(teacherArrayAdapter);
			
			Button addButton = (Button)view.findViewById(R.id.addButton);
			addButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View button2) {
					database.addAssociationCourseTeacher(new AssociationCourseTeacher(database.generateNextId(Database.ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME, Database.ASSOCIATION_COURSE_TEACHER_ID), 
							((Course)courseSpinner.getSelectedItem()).getCourseId(), 
							((Teacher)teacherSpinner.getSelectedItem()).getTeacherId()));
					dialog.dismiss();
				}
			});				
			
		}		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		database = Database.getInstance(this);
		
		final Spinner course1NameSpinner = (Spinner)findViewById(R.id.course1NameSpinner);
		final ArrayAdapter<Course> course1ArrayAdapter = new ArrayAdapter<Course>(context, android.R.layout.simple_spinner_item, database.selectCourses(-1));
		course1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		course1NameSpinner.setAdapter(course1ArrayAdapter);
		
		final Spinner teacher1NameSpinner = (Spinner)findViewById(R.id.teacher1NameSpinner);
		final ArrayAdapter<Teacher> teacher1ArrayAdapter = new ArrayAdapter<Teacher>(context, android.R.layout.simple_spinner_item, database.selectTeachers(-1));
		teacher1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		teacher1NameSpinner.setAdapter(teacher1ArrayAdapter);
		
		Button courseAddButton = (Button)findViewById(R.id.courseAddButton);
		courseAddButton.setOnClickListener(new CourseButtonListener(course1ArrayAdapter));
		
		Button teacherAddButton = (Button)findViewById(R.id.teacherAddButton);
		teacherAddButton.setOnClickListener(new TeacherButtonListener(teacher1ArrayAdapter));		
		
		Button associationCourseTeacherAddButton = (Button)findViewById(R.id.associationCourseTeacherAddButton);
		associationCourseTeacherAddButton.setOnClickListener(new AssociationCourseTeacherButtonListener());
		
		final ListView teacherListView = (ListView)findViewById(R.id.teacherListView);
		
		Button searchTeacherButton = (Button)findViewById(R.id.teacherSearchButton);
		searchTeacherButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Course course = (Course)course1NameSpinner.getSelectedItem();
				List<Teacher> teacher2List = database.selectTeachers(course.getCourseId());
				ArrayAdapter<Teacher> teacher2ArrayAdapter = new ArrayAdapter<Teacher>(context, android.R.layout.simple_list_item_1, teacher2List);
				teacherListView.setAdapter(teacher2ArrayAdapter);				
			}
		});
		
		final ListView courseListView = (ListView)findViewById(R.id.courseListView);
		
		Button searchCourseButton = (Button)findViewById(R.id.courseSearchButton);
		searchCourseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Teacher teacher = (Teacher)teacher1NameSpinner.getSelectedItem();
				List<Course> course2List = database.selectCourses(teacher.getTeacherId());
				ArrayAdapter<Course> course2ArrayAdapter = new ArrayAdapter<Course>(context, android.R.layout.simple_list_item_1, course2List);
				courseListView.setAdapter(course2ArrayAdapter);				
			}
		});		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
