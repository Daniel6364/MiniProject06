package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 상품관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addProductView.do")
	public String addUserView() throws Exception {

		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct( @ModelAttribute("product") Product product, 
			HttpServletRequest request ) throws Exception {

		System.out.println("/addProduct.do");

		//Business Logic
		product.setManuDate(request.getParameter("manuDate").replace("-", ""));
		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct( @RequestParam("prodNo") String prodNo , Model model, 
			HttpSession session, @RequestParam("menu") String menu ) throws Exception {
		
		System.out.println("/getProduct.do");

		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		if (menu.equals("manage")) {
			return "redirect:/updateProductView.do?prodNo="+prodNo;
		} else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") String prodNo , 
			Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product , 
			Model model, HttpServletRequest request ) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		product.setManuDate(request.getParameter("manuDate").replace("-", ""));
		productService.updateProduct(product);
		model.addAttribute(product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , 
			Model model, @RequestParam("menu") String menu, 
				@RequestParam(value="lowPriceCondition", required=false) String lowPriceCondition,
				@RequestParam(value="highPriceCondition", required=false) String highPriceCondition ) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() == 0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		System.out.println("1 ="+ lowPriceCondition);
		System.out.println("2 ="+ highPriceCondition);
		
		if (search.getSearchPrice() != null) {
			if (lowPriceCondition.matches("lowPrice") && highPriceCondition == null) {
				System.out.println("2-1 ="+ lowPriceCondition);
				System.out.println("2-2 ="+ highPriceCondition);
				search.setSearchPrice(lowPriceCondition);
			} else	{
				System.out.println("3-1 ="+ lowPriceCondition);
				System.out.println("3-2 ="+ highPriceCondition);
				search.setSearchPrice(highPriceCondition);
			}
		}
		
		System.out.println("[price] ==> " + search.getSearchPrice());
			
		// Business logic 수행
		Map<String , Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);

		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);

		return "forward:/product/listProduct.jsp";
		
	}
}