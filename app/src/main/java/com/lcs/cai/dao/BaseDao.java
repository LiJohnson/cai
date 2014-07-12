package com.lcs.cai.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

/**
 * Created by lcs on 7/11/2014.
 */
public class BaseDao<T> extends SQLiteOpenHelper {
	private Class cls;

	public BaseDao(Class cls , Context context){
		this( context , "abs.db" );
		this.cls = cls;
	}

	public BaseDao(Context context, String name ) {
		this(context, name, null, 1);
	}

	public BaseDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 获取一个记录
	 * @param id
	 * @return
	 */
	public T get(int id){
		String tableName = getClassName( this.cls.getName() );
		String[] val = {id+""};
		Cursor query = this.getReadableDatabase().rawQuery("SELECT * FROM " + tableName  + " WHERE " + tableName + "Id = ?" , val);
		if( query.getCount() == 0 ){
			return null;
		}

		if( query.isBeforeFirst() ){
			query.moveToNext();
		}
		T t = this.newInstance(query);
		query.close();
		return t;
	}

	public int insert( T t ){
		if( t == null )return 0;
		String tableName =  getClassName( this.cls.getName() );
		String pk = tableName + "Id";
		ContentValues contentValues = new ContentValues();
		Field[] fields = t.getClass().getDeclaredFields();

		for( Field f : fields ){
			String colName = f.getName();
			Class fieldClass = f.getClass();
			if( colName.equals( pk ) ){
				continue;
			}

			try {
				Method m = this.cls.getDeclaredMethod( "get" + colName.substring(0,1).toUpperCase() + colName.substring(1) );
				Object val = m.invoke( t );
				if( val == null ){
					continue;
				}
				if( int.class.equals( fieldClass ) ){
					contentValues.put(colName ,  (Integer)val );
				}else if( float.class.equals(fieldClass) ){
					contentValues.put(colName , (Float)val);
				}else if( long.class.equals( fieldClass ) ){
					contentValues.put(colName , (Long)val);
				}else if( double.class.equals( fieldClass ) ){
					contentValues.put(colName , (Double)val);
				}else{
					contentValues.put(colName , val.toString());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}catch ( NoSuchMethodException e){
				e.printStackTrace();
			}catch ( InvocationTargetException e){
				e.printStackTrace();
			}
		}
		return (int)this.getWritableDatabase().insert( tableName , null , contentValues );
	}

	/**
	 * 生成创建表的sql
	 * @return
	 */
	public String getCreateTableSql(){
		String tableName = getClassName(this.cls.getName());
		String sql = "CREATE TABLE " + tableName ;
		String pk = tableName + "Id";
		Field[] cols = this.cls.getDeclaredFields();

		sql += "(";
		for( Field f : cols ){
			String colName = f.getName();
			String colType = getClassName(f.getType().getName());
			if( colName.equals( pk ) ){
				sql += pk + " INTEGER PRIMARY KEY AUTOINCREMENT ,";
			}else{
				sql += colName + " " + getSqlType(colType) +" ," ;
			}
		}
		sql = sql.substring(0,sql.length()-1) + ");";
		return sql;
	}

	/**
	 * 获取一个class的名字：去除包名，首字母小写
	 * @param className
	 * @return
	 */
	private static String getClassName( String className ){
		int index = className.lastIndexOf(".");
		if( index != -1 ){
			className = className.substring(index+1);
		}
		return className.substring(0,1).toLowerCase() + className.substring(1);
	}

	/**
	 * 将java类型映射为sql的类型
	 * @param javaType
	 * @return
	 */
	private static String getSqlType( String javaType ){
		if( javaType == "string" ){
			return "text";
		}else  {
			return javaType;
		}
	}

	private  T newInstance(Cursor q){
		T t = null;
		try {
			t = (T)this.cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if( t == null )return t;

		Field[] fields = this.cls.getDeclaredFields();
		for( Field f : fields ){
			String colName = f.getName();
			int index = q.getColumnIndex( colName );
			Class fieldClass = f.getClass();

			Object val = null;

			if( int.class.equals( fieldClass ) ){
				val = q.getInt( index );
			}else if( float.class.equals(fieldClass) ){
				val = q.getFloat( index );
			}else if( long.class.equals( fieldClass ) ){
				val = q.getLong( index );
			}else if( double.class.equals( fieldClass ) ){
				val = q.getDouble( index );
			}else{
				val = q.getString( index );
			}
			if( val == null ){
				continue;
			}

			try {
				Method m = this.cls.getDeclaredMethod( "set" + colName.substring(0,1).toUpperCase() + colName.substring(1) );
				m.invoke( t , val );
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return t;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL( this.getCreateTableSql() );
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}
}
