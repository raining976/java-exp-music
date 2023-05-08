package dao;

import java.util.List;

/**
 * 数据表操作基础接口 
 *
 * @param <T>
 */
public interface BaseDao<T> {
	/**
	 * 增
	 * 
	 * @param t
	 */
	void insert(T t);

	/**
	 * 删
	 * 
	 * @param id
	 */
	void delete(int id);

	/**
	 * 改
	 * 
	 * @param t
	 */
	void update(T t);

	/**
	 * 查（查所有）
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 查（基于ID查单条）
	 * 
	 * @param id
	 * @return
	 */
	T findById(int id);
}
