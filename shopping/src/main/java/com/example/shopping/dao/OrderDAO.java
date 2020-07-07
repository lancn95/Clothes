package com.example.shopping.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.Order;
import com.example.shopping.entities.OrderDetail;
import com.example.shopping.entities.Product;
import com.example.shopping.model.CartInfo;
import com.example.shopping.model.CartLineInfo;
import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;

@Repository
public class OrderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;

	public int getOrderMax() {
		String sql = "Select Max(o.orderNum) from " + Order.class.getName() + " o ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);

		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOder(CartInfo cartInfo) {
		Session session = sessionFactory.getCurrentSession();
		int orderNum = this.getOrderMax() + 1;

		Order order = new Order();
		order.setId(UUID.randomUUID().toString());
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());
		order.setCustomerName(cartInfo.getCustomerInfo().getName());
		order.setCustomerEmail(cartInfo.getCustomerInfo().getEmail());
		order.setCustomerAddress(cartInfo.getCustomerInfo().getAddress());
		order.setCustomerPhone(cartInfo.getCustomerInfo().getPhone());

		session.persist(order);

		for (CartLineInfo line : cartInfo.getCartLines()) {
			OrderDetail orderDetail = new OrderDetail();

			orderDetail.setId(UUID.randomUUID().toString());
			orderDetail.setOrder(order);
			orderDetail.setQuantity(line.getQuantity());
			orderDetail.setPrice(line.getProductInfo().getPrice());
			orderDetail.setAmount(line.getAmount());

			String code = line.getProductInfo().getCode();
			Product product = productDAO.findProduct(code);
			orderDetail.setProduct(product);

			session.persist(orderDetail);

		}

		// số order
		cartInfo.setOrderNum(orderNum);

		session.flush();
	}

	@Transactional(rollbackFor = Exception.class)
	public List<OrderInfo> findAll() {
		try {
			String sql = "Select new " + OrderInfo.class.getName()//
					+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
					+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
					+ Order.class.getName() + " ord "//
					+ " order by ord.orderNum desc";
			Session session = this.sessionFactory.getCurrentSession();

			Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
			return query.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	 public Order findOrder(String orderId) {
	        Session session = this.sessionFactory.getCurrentSession();
	        return session.find(Order.class, orderId);
	    }
	 	
	@Transactional(rollbackFor = Exception.class)
    public OrderInfo getOrderInfo(String orderId) {
        Order order = this.findOrder(orderId);
        if (order == null) {
            return null;
        }
        return new OrderInfo(order.getId(), order.getOrderDate(), //
                order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
    }
	 
	@Transactional(rollbackFor = Exception.class)
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        String sql = "Select new " + OrderDetailInfo.class.getName() //
                + "(d.id, d.product.code, d.product.name , d.quantity,d.price,d.amount) "//
                + " from " + OrderDetail.class.getName() + " d "//
                + " where d.order.id = :orderId ";
 
        Session session = this.sessionFactory.getCurrentSession();
        Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
        query.setParameter("orderId", orderId);
 
        return query.getResultList();
    }
}
