package ro.pub.systems.pdsd.ex3coursesteachers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "ex3coursesteachers";
	public static final int DATABASE_VERSION = 1;
	
	
	public static final String ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME = "associations_courses_teachers";
	public static final String ASSOCIATION_COURSE_TEACHER_ID = "association_course_teacher_id";
	public static final String COURSES_TABLE_NAME = "courses";
	public static final String COURSE_ID = "course_id";
	public static final String COURSE_NAME = "name";
	public static final String TEACHERS_TABLE_NAME = "teachers";
	public static final String TEACHER_ID = "teacher_id";
	public static final String TEACHER_NAME = "name";
	
	private static Database instance;
	
	public static Database getInstance(Context context) {
		if (instance == null)
			instance = new Database(context.getApplicationContext());
		
		return instance;
	}

	private Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_COURSES_TABLE = "CREATE TABLE " + COURSES_TABLE_NAME + "("
				+ COURSE_ID + " INTEGER PRIMARY KEY, "
				+ COURSE_NAME + " TEXT)";
		String CREATE_TEACHERS_TABLE = "CREATE TABLE " + TEACHERS_TABLE_NAME + "("
				+ TEACHER_ID + " INTEGER PRIMARY KEY, "
				+ TEACHER_NAME + " TEXT)";	
		String CREATE_ASSOCIATIONS_COURSES_TEACHERS_TABLE = "CREATE TABLE " + ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME + "("
				+ ASSOCIATION_COURSE_TEACHER_ID + " INTEGER PRIMARY KEY, "
				+ COURSE_ID + " INTEGER, " 
				+ TEACHER_ID + " INTEGER, "
				+ "FOREIGN KEY (" + COURSE_ID + ") REFERENCES " + COURSES_TABLE_NAME + " (" + COURSE_ID + "), "
				+ "FOREIGN KEY (" + TEACHER_ID + ") REFERENCES " + TEACHERS_TABLE_NAME + " (" + TEACHER_ID + "))";
		db.execSQL(CREATE_COURSES_TABLE);
		db.execSQL(CREATE_TEACHERS_TABLE);
		db.execSQL(CREATE_ASSOCIATIONS_COURSES_TEACHERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + COURSES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TEACHERS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME);
		onCreate(db);
	}
	
	public void addCourse(Course course) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(COURSE_ID, course.getCourseId());
		contentValues.put(COURSE_NAME, course.getName());
		
		db.insert(COURSES_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	
	public void addTeacher(Teacher teacher) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(TEACHER_ID, teacher.getTeacherId());
		contentValues.put(TEACHER_NAME, teacher.getName());
		
		db.insert(TEACHERS_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	
	public void addAssociationCourseTeacher(AssociationCourseTeacher associationCourseTeacher) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(ASSOCIATION_COURSE_TEACHER_ID, associationCourseTeacher.getAssociationCourseTeacherId());
		contentValues.put(COURSE_ID, associationCourseTeacher.getCourseId());
		contentValues.put(TEACHER_ID, associationCourseTeacher.getTeacherId());
		
		db.insert(ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	
	public List<Teacher> selectTeachers(int courseId) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_TEACHERS_QUERY;
		if (courseId != -1)
			SELECT_TEACHERS_QUERY = "SELECT t." + TEACHER_ID + ", " + TEACHER_NAME + " FROM " + TEACHERS_TABLE_NAME + " t, " + ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME + " act"
				+ " WHERE act." + COURSE_ID + "=" + courseId + " AND t." + TEACHER_ID + " = act." + TEACHER_ID;
		else
			SELECT_TEACHERS_QUERY = "SELECT * FROM " + TEACHERS_TABLE_NAME;
		
		Log.d("MY_TAG", SELECT_TEACHERS_QUERY);
		Cursor cursor = db.rawQuery(SELECT_TEACHERS_QUERY, null);
		ArrayList<Teacher> result = new ArrayList<Teacher>();
		if (cursor.moveToFirst()) {			
			do {
				Teacher teacher = new Teacher();
				teacher.setTeacherId(Integer.parseInt(cursor.getString(0)));
				teacher.setName(cursor.getString(1));
				result.add(teacher);
			} while (cursor.moveToNext());
		}
		return result;
	}
	
	public List<Course> selectCourses(int teacherId) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_COURSES_QUERY;
		if (teacherId != -1)
			SELECT_COURSES_QUERY = "SELECT c." + COURSE_ID + ", "+ COURSE_NAME + " FROM " + COURSES_TABLE_NAME + " c, " + ASSOCIATIONS_COURSE_TEACHER_TABLE_NAME + " act"
				+ " WHERE act." + TEACHER_ID + "=" + teacherId + " AND c." + COURSE_ID + " = act." + COURSE_ID;
		else
			SELECT_COURSES_QUERY = "SELECT * FROM " + COURSES_TABLE_NAME; 
		Cursor cursor = db.rawQuery(SELECT_COURSES_QUERY, null);
		ArrayList<Course> result = new ArrayList<Course>();
		if (cursor.moveToFirst()) {
			do {
				Course course = new Course();
				course.setCourseId(Integer.parseInt(cursor.getString(0)));
				course.setName(cursor.getString(1));
				result.add(course);
			} while (cursor.moveToNext());
		}
		return result;
	}	
	
	public int generateNextId(String tableName, String primaryKeyName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_MAX_ID_FROM_TABLE = "SELECT MAX(" + primaryKeyName + ") FROM " + tableName;
		Cursor cursor = db.rawQuery(SELECT_MAX_ID_FROM_TABLE, null);
		if (cursor != null && cursor.moveToFirst() && cursor.getString(0) != null)
			return Integer.parseInt(cursor.getString(0)) + 1;
		
		return 0;
	}
	
} 
