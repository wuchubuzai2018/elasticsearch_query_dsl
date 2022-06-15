# 一、项目名称：

Elasticsearch_Query_DSL

# 二、项目描述

从ES驱动源码中提取的ElasticSearch Query DSL构造项目，对源码中的JSON字符串构造进行了修改，修改为FastJSON进行序列化输出，轻量级DSL查询语句构造，无严重依赖，可用于自己的项目中进行代码构造

目前支持常用的一些DSL的生成配置。

# 三、测试语句

测试语句如下：

```java
public static void main(String[] args) {
		/* */
		//1.查询全部的语法
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
		System.out.println(queryBuilder.toString());
		//2.查询指定字段的语法
		QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("actors","value").operator(Operator.AND);
		System.out.println(queryBuilder2.toString());
		//3.term指定字段的语法
		QueryBuilder queryBuilder3 = QueryBuilders.termQuery("actors",24.44);
		System.out.println(queryBuilder3.toString());
		//4.多字段的查询
		QueryBuilder queryBuilder4 = QueryBuilders.multiMatchQuery("Hello World", "name","age");
		System.out.println(queryBuilder4.toString());
		//5.match phrase查询
		QueryBuilder queryBuilder5 = QueryBuilders.matchPhraseQuery("title", "我是标题");
		System.out.println(queryBuilder5.toString());
		//6.match phrase prefix查询
		QueryBuilder queryBuilder6 = QueryBuilders.matchPhrasePrefixQuery("title", "我是标题");
		System.out.println(queryBuilder6.toString());
		//7.ids查询
		QueryBuilder queryBuilder7 = QueryBuilders.idsQuery().addIds("11","12");
		System.out.println(queryBuilder7.toString());
		//8.range query
		QueryBuilder queryBuilder8 = QueryBuilders.rangeQuery("age").gt(3).lt(10);
		System.out.println(queryBuilder8.toString());
		//9.bool query
		QueryBuilder queryBuilder9 = QueryBuilders.boolQuery()
		            .must(QueryBuilders.termQuery("user", "kimchy"))
		            .mustNot(QueryBuilders.termQuery("message", "nihao"))
		            .should(QueryBuilders.termQuery("gender", "male"));
			System.out.println(queryBuilder9.toString());
		//10.wildcard query
		QueryBuilder queryBuilder10 = QueryBuilders.wildcardQuery("name",  
	            "*jack*");
		System.out.println(queryBuilder10.toString());
		 //11.termsQuery
        QueryBuilder queryBuilder11 = QueryBuilders.termsQuery("user", "jnkins","long");
        System.out.println(queryBuilder11.toString());
        //12.query_string查询
        QueryBuilder queryBuilder12 = QueryBuilders.queryStringQuery("趋势").defaultOperator(Operator.OR)
        						.field("label").field("sis").field("name").field("dim");
        System.out.println(queryBuilder12.toString());
	}

```

# 四、项目维护参与

欢迎感兴趣的小伙伴参与项目的维护与开发。

# 五、公众号与知识星球

作者公众号「**无处不在的技术**」，欢迎扫码关注，**定期分享技术领域相关知识及开发资源**。

作者知识星球「**Java&导师&B端&学生&外包开发圈**」。
