###############
SELECT * FROM t_order;
SELECT * FROM t_order_item;
SELECT * FROM t_product;
SELECT * FROM t_category;
SELECT * FROM t_category_product;
#
###
######
###########
#####################
##################################
#############################################  
DROP PROCEDURE IF EXISTS `p_select_branchid_order_product_analyze` ;
DROP TEMPORARY TABLE IF EXISTS `t_order_item_temp_by_product` ;

DELIMITER //

CREATE PROCEDURE `p_select_branchid_order_product_analyze` (
  IN in_hqId BIGINT,
  IN in_dateFrom DATE,
  IN in_dateTo DATE,
  IN in_branchId BIGINT,
  IN in_categoryId BIGINT
)
BEGIN
 DECLARE var_orderId BIGINT;
 DECLARE var_itemId BIGINT;
 DECLARE var_orderEnd BIT DEFAULT FALSE;
 DECLARE var_orderCount BIGINT;
 DECLARE cur_orderId CURSOR FOR 
 SELECT id FROM t_order 
 WHERE STATUS = 'COMPLETE'
 AND (in_dateTo IS NULL OR DATE(created_at) <= in_dateTo) 
 AND (in_dateFrom IS NULL OR DATE(created_at) >= in_dateFrom) AND (in_hqId IS NULL OR hq_id = in_hqId) AND (in_branchId IS NULL OR branch_id = in_branchId);
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET var_orderEnd = TRUE;
 
 SELECT COUNT(*) FROM t_order
 WHERE STATUS = 'COMPLETE'
 AND original_bill IS NULL
 AND (in_dateTo IS NULL OR DATE(created_at) <= in_dateTo) 
 AND (in_dateFrom IS NULL OR DATE(created_at) >= in_dateFrom) AND (in_hqId IS NULL OR hq_id = in_hqId) AND (in_branchId IS NULL OR branch_id = in_branchId)
 INTO var_orderCount;
 
 CREATE TEMPORARY TABLE t_order_item_temp_by_product (
      product_id BIGINT NULL,
      product_name VARCHAR(50) NULL,
      product_count BIGINT NULL,
      item_price DECIMAL(10,2) NULL
 );
 OPEN cur_orderId;
 order_loop:
 LOOP
     FETCH cur_orderId INTO var_orderId;
     IF var_orderEnd = TRUE
      THEN
        SET var_orderEnd = FALSE;
        LEAVE order_loop;
      END IF;
      BEGIN
	  DECLARE var_itemEnd BIT DEFAULT FALSE;
	  DECLARE cur_itemId CURSOR FOR
	  SELECT a.id FROM t_order_item a
	  LEFT JOIN 
	  (SELECT DISTINCT(product_id) AS product_id FROM t_category_product WHERE (in_categoryId IS NULL OR category_id = in_categoryId)) b
	  ON a.product_id = b.product_id
	  WHERE a.order_id = var_orderId ;
	  
	  DECLARE CONTINUE HANDLER FOR NOT FOUND SET var_itemEnd = TRUE;
	  OPEN cur_itemId;
	  item_loop:
	  LOOP
	        FETCH cur_itemId INTO var_itemId;
		IF var_itemEnd = TRUE
		THEN
		   SET var_itemEnd = FALSE;
		   LEAVE item_loop;
		END IF;
		INSERT INTO t_order_item_temp_by_product(product_id,product_name,product_count,item_price) 
		(SELECT a.product_id AS product_id , b.name AS product_name , a.count AS product_count,a.price AS item_price
		 FROM t_order_item a LEFT JOIN t_product b ON a.product_id = b.id WHERE a.id = var_itemId);
	  END LOOP;
	  CLOSE cur_itemId;
     END;
 END LOOP;
 CLOSE cur_orderId;
 SELECT product_id AS productId ,product_name AS productName ,SUM(product_count) AS productQuantity,COUNT(product_id) AS itemQuantity,
	SUM(CASE WHEN product_count < 0
		THEN -item_price
		ELSE item_price
		END) AS saleAmount, var_orderCount AS orderCount
FROM t_order_item_temp_by_product GROUP BY product_id ,product_name;
DROP TEMPORARY TABLE IF EXISTS `t_order_item_temp_by_product`;
END //

CALL p_select_branchid_order_product_analyze(1,NULL,NULL,NULL,2); 

#########
#################
################################   
DROP PROCEDURE IF EXISTS `p_select_branchid_order_category_analyze` ;
DROP TEMPORARY TABLE IF EXISTS `t_order_item_temp_by_category` ;

DELIMITER //

CREATE PROCEDURE `p_select_branchid_order_category_analyze` (
  IN in_hqId BIGINT,
  IN in_dateFrom DATE,
  IN in_dateTo DATE,
  IN in_branchId BIGINT
)
BEGIN
 DECLARE var_orderId BIGINT;
 DECLARE var_itemId BIGINT;
 DECLARE var_orderEnd BIT DEFAULT FALSE;
 DECLARE var_orderCount BIGINT;
 DECLARE cur_orderId CURSOR FOR 
 SELECT id FROM t_order 
 WHERE STATUS = 'COMPLETE'
 AND  hq_id = in_hqId
 AND (in_dateTo IS NULL OR DATE(created_at) <= in_dateTo) 
 AND (in_dateFrom IS NULL OR DATE(created_at) >= in_dateFrom) AND (in_branchId IS NULL OR branch_id = in_branchId);
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET var_orderEnd = TRUE;
 
 SELECT COUNT(*) FROM t_order
 WHERE STATUS = 'COMPLETE'
 AND hq_id = in_hqId
 AND original_bill IS NULL
 AND (in_dateTo IS NULL OR DATE(created_at) <= in_dateTo) 
 AND (in_dateFrom IS NULL OR DATE(created_at) >= in_dateFrom) AND (in_branchId IS NULL OR branch_id = in_branchId)
 INTO var_orderCount;

 CREATE TEMPORARY TABLE t_order_item_temp_by_category (
      product_id BIGINT NULL,
      category_id BIGINT NULL,
      category_name VARCHAR(50) NULL,
      product_count BIGINT NULL,
      item_price DECIMAL(10,2) NULL
 );
 OPEN cur_orderId;
 order_loop:
 LOOP
     FETCH cur_orderId INTO var_orderId;
     IF var_orderEnd = TRUE
      THEN
        SET var_orderEnd = FALSE;
        LEAVE order_loop;
      END IF;
      BEGIN
	  DECLARE var_itemEnd BIT DEFAULT FALSE;
	  DECLARE cur_itemId CURSOR FOR
	  SELECT id FROM t_order_item WHERE order_id = var_orderId;
	  DECLARE CONTINUE HANDLER FOR NOT FOUND SET var_itemEnd = TRUE;
	  OPEN cur_itemId;
	  item_loop:
	  LOOP
	        FETCH cur_itemId INTO var_itemId;
		IF var_itemEnd = TRUE
		THEN
		   SET var_itemEnd = FALSE;
		   LEAVE item_loop;
		END IF;
		INSERT INTO t_order_item_temp_by_category(product_id,category_id,category_name,product_count,item_price)
		(SELECT a.product_id AS product_id ,b.category_id AS category_id , c.name AS category_name,a.count AS product_count,a.price AS item_price
		FROM t_order_item a LEFT JOIN t_category_product b ON a.product_id = b.product_id LEFT JOIN t_category c ON b.category_id = c.id WHERE a.id = var_itemId);
	  END LOOP;
	  CLOSE cur_itemId;
     END;
 END LOOP;
 CLOSE cur_orderId;
 SELECT category_id AS categoryId,category_name AS categoryName,COUNT(product_id) AS itemQuality,SUM(product_count) AS productQuality
	,SUM(
	CASE WHEN product_count<0
	THEN -item_price
	ELSE item_price
	END
	) AS saleAmount ,var_orderCount AS orderCount
 FROM t_order_item_temp_by_category GROUP BY category_id,category_name;
 DROP TEMPORARY TABLE IF EXISTS `t_order_item_temp_by_category`;
END //

CALL p_select_branchid_order_category_analyze(1,NULL,NULL,NULL);

SELECT * FROM t_order_item_temp_by_category;


SELECT a.product_id AS product_id ,b.category_id AS category_id , c.name AS category_name,a.count AS product_count,a.price AS item_price
FROM t_order_item a LEFT JOIN t_category_product b ON a.product_id = b.product_id LEFT JOIN t_category c ON b.category_id = c.id;


CALL p_select_branchid_order_product_analyze('2016-01-03 00:00:00','2016-01-7 00:00:00',1,1,2); 

CALL p_select_branchid_order_product_analyze(1,NULL,NULL,NULL,NULL); 

SELECT id FROM t_order_item WHERE order_id IN
(SELECT id FROM t_order 
 WHERE STATUS = 'COMPLETE'
 AND ('2016-01-07 00:00:00' IS NULL OR DATE(created_at) <= '2016-01-07 00:00:00') 
 AND ('2016-01-03 00:00:00' IS NULL OR DATE(created_at) <= '2016-01-03 00:00:00') AND hq_id = 1 AND branch_id = 1); 
 
SELECT * FROM t_order_item a LEFT JOIN t_category_product b ON a.product_id = b.product_id WHERE b.category_id =1 AND a.order_id = 1;
 
################################
DROP PROCEDURE IF EXISTS `p_product_analyze_detail_by_day` ;
DROP TEMPORARY TABLE IF EXISTS `t_order_item_temp_by_day` ;

DELIMITER //

CREATE PROCEDURE p_product_analyze_detail_by_day (
	IN in_hqId BIGINT,
	IN in_branchId BIGINT,
	IN in_productId BIGINT,
	IN in_dateFrom DATE ,
	IN in_dateTo DATE,
	IN in_categoryId BIGINT
)
BEGIN
 DECLARE var_orderId BIGINT;
 DECLARE var_itemId BIGINT;
 DECLARE var_orderEnd BIT DEFAULT FALSE;
 DECLARE var_orderCount BIGINT;
 DECLARE cur_orderId CURSOR FOR 
 SELECT id FROM t_order 
 WHERE STATUS = 'COMPLETE'
 AND (in_dateTo IS NULL OR DATE(created_at) <= in_dateTo) 
 AND (in_dateFrom IS NULL OR DATE(created_at) >= in_dateFrom) AND (in_hqId IS NULL OR hq_id = in_hqId) AND (in_branchId IS NULL OR branch_id = in_branchId);
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET var_orderEnd = TRUE;
 
 SELECT COUNT(*) FROM t_order
 WHERE STATUS = 'COMPLETE'
 AND original_bill IS NULL
 AND (in_dateTo IS NULL OR DATE(created_at) <= in_dateTo) 
 AND (in_dateFrom IS NULL OR DATE(created_at) >= in_dateFrom) AND (in_hqId IS NULL OR hq_id = in_hqId) AND (in_branchId IS NULL OR branch_id = in_branchId)
 INTO var_orderCount;
 
 CREATE TEMPORARY TABLE t_order_item_temp_by_day (
      product_id BIGINT NULL,
      product_name VARCHAR(50) NULL,
      product_count BIGINT NULL,
      item_price DECIMAL(10,2) NULL,
      service_type VARCHAR(20) NULL,
      created_at DATETIME NULL
 );
 OPEN cur_orderId;
 order_loop:
 LOOP
     FETCH cur_orderId INTO var_orderId;
     IF var_orderEnd = TRUE
      THEN
        SET var_orderEnd = FALSE;
        LEAVE order_loop;
      END IF;
      BEGIN
	  DECLARE var_itemEnd BIT DEFAULT FALSE;
	  DECLARE cur_itemId CURSOR FOR
	  SELECT a.id FROM t_order_item a
	  LEFT JOIN 
	  (SELECT DISTINCT(product_id) AS product_id FROM t_category_product WHERE (in_categoryId IS NULL OR category_id = in_categoryId)) b
	  ON a.product_id = b.product_id
	  WHERE a.order_id = var_orderId AND (in_productId IS NULL OR a.product_id = in_productId);
	  
	  DECLARE CONTINUE HANDLER FOR NOT FOUND SET var_itemEnd = TRUE;
	  OPEN cur_itemId;
	  item_loop:
	  LOOP
	        FETCH cur_itemId INTO var_itemId;
		IF var_itemEnd = TRUE
		THEN
		   SET var_itemEnd = FALSE;
		   LEAVE item_loop;
		END IF;
		INSERT INTO t_order_item_temp_by_day(product_id,product_name,product_count,item_price,service_type) 
		(SELECT a.product_id AS product_id , b.name AS product_name , a.count AS product_count,a.price AS item_price,
		 a.service_type AS service_type
		 FROM t_order_item a LEFT JOIN t_product b ON a.product_id = b.id
		 WHERE a.id = var_itemId);
	  END LOOP;
	  CLOSE cur_itemId;
     END;
 END LOOP;
 CLOSE cur_orderId;
 SELECT option_name,COUNT(product_id) FROM t_order_item_temp_by_day GROUP BY option_name;
 DROP TEMPORARY TABLE IF EXISTS t_order_item_temp_by_day;
END //

CALL p_product_analyze_detail_by_day(1,NULL,NULL,NULL,NULL,NULL);

SELECT * FROM t_order_item_temp_by_option;

SELECT service_type,COUNT(product_id) FROM t_order_item_temp_by_productId GROUP BY service_type;

SELECT a.product_id AS product_id , b.name AS product_name , a.count AS product_count,a.price AS item_price,
		 a.service_type AS service_type,d.name AS option_name
		 FROM t_order_item a LEFT JOIN t_product b ON a.product_id = b.id LEFT JOIN t_order_item_option c ON a.id = c.order_item_id
		 LEFT JOIN t_product_option d ON c.option_id = d.id
		 WHERE a.id = var_itemId;
 
    