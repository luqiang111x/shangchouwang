<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/include-head.jsp"%>
<link href="/css/pagination.css" rel="stylesheet">
<script type="text/javascript" src="/jquery/jquery.pagination.js"></script>
<script type="text/javascript">

		$(function(){
			// 调用后面对的函数，对我们的页码导航条进行初始化操作
			initPagination();

		});
	
		// 生成页码导航条函数
		function initPagination(){
			
			// 获取总记录数 
			var totalRecord = ${requestScope.pageInfo.total};
			
			// 页面显示多少条记录
			var pageSize =  ${requestScope.pageInfo.pageSize};
			
			//显示当前页码，pagination工作从0页开始所有获取到的页码要-1
			var pageNum =  ${requestScope.pageInfo.pageNum-1};
			
			//声明一个JSON对象来存储pagination设置的属性
			var properties ={
					"num_edge_entries":3,			   		//边缘页数
					"num_display_entries": 4,        		//主体页数
					"callback": pageSelectCallback,  		//回调函数，当用户点击翻页按钮时调用
					"items_per_page":pageSize,            //页面显示多少条数据
					"current_page":pageNum,				//显示当前页码
					"prev_text": "上一页",
					"next_text": "下一页"
			};
			
			// 生成我们的页码导航条
			$("#Pagination").pagination(totalRecord,properties);
		
		}
	
		// 用户点击页码时，实现页面跳转
		// pageIndex时Pagination传给我们的那个从0开始的页码值
		function pageSelectCallback(pageIndex,jQuery){
			
			// 根据pageIndex计算pageNum的值
			var pageNum = pageIndex+1;
			// 跳转页面
			window.location.href="admin/get/page.html?pageNum="+pageNum+"&keyword=${param.keyword}";
			
			// 由于每个页码都是超链接，所以我们取消超链接的默认行为
			return false;
		}
	
</script>


<body>

	<%@include file="/WEB-INF/include-nav.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/include-sidebar.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-th"></i> 数据列表
						</h3>
					</div>
					<div class="panel-body">
						<form action="admin/get/page.html" method="post" class="form-inline" role="form" style="float: left;">
							<div class="form-group has-feedback">
								<div class="input-group">
									<div class="input-group-addon">查询条件</div>
									<input class="form-control has-success"  name="keyword" type="text"
										placeholder="请输入查询条件">
								</div>
							</div>
							<button type="submit" class="btn btn-warning">
								<i class="glyphicon glyphicon-search"></i> 查询
							</button>
						</form>
						<button type="button" class="btn btn-danger"
							style="float: right; margin-left: 10px;">
							<i class=" glyphicon glyphicon-remove"></i> 删除
						</button>

						<a style="float: right;" href="admin/to/add/page.html" class="btn btn-primary"><i class="glyphicon glyphicon-plus"></i>新增</a>
						<br>
						<hr style="clear: both;">
						<div class="table-responsive">
							<table class="table  table-bordered">
								<thead>
									<tr>
										<th width="30">#</th>
										<th width="30"><input type="checkbox"></th>
										<th>账号</th>
										<th>名称</th>
										<th>邮箱地址</th>
										<th width="100">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${empty requestScope.pageInfo.list }">
										<tr>
											<td colspan="6" style="text-align: center; font: bold; color: #ff0000;">抱歉！没有查询到您要的数据！</td>
										</tr>
									</c:if>
									<c:if test="${!empty requestScope.pageInfo.list }">
										<c:forEach items="${requestScope.pageInfo.list }" var="admin"
											varStatus="myStatus">
											<tr>
												<td>${myStatus.count}</td>
												<td><input type="checkbox"></td>
												<td>${admin.loginAcct }</td>
												<td>${admin.userName }</td>
												<td>${admin.email }</td>
												<td>
													<a href="assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></a>
													<a href="admin/to/edit/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></a>
													<a id="remove" class="btn btn-danger btn-xs" href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html">
														<i class=" glyphicon glyphicon-remove"></i>
													</a>
												</td>
											</tr>
										</c:forEach>
									</c:if>

								</tbody>
								<tfoot>
									<tr>
										<td colspan="6" align="center">
											<div id="Pagination" class="pagination">
											</div>
										</td>
									</tr>

								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>

</body>
</html>