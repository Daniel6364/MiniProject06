package com.model2.mvc.web.purchase;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.product.ProductService;


//==> 회원관리 Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	//setter Method 구현 않음
		
	public PurchaseController(){
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
	
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView(@ModelAttribute("product") Product product,
			@RequestParam("prod_no") String prodNo, Model model) throws Exception {

		System.out.println("/addPurchaseView.do");
		
		product = productService.getProduct(Integer.parseInt(prodNo));
		
		model.addAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase,  
			@RequestParam("prodNo") String prodNo, @RequestParam("buyerId") String buyerId ) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
		
		purchase.setPurchaseProd(productService.getProduct(Integer.parseInt(prodNo)));
		purchase.setBuyer(userService.getUser(buyerId));
		purchaseService.addPurchase(purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("tranNo") String tranNo , Model model ) throws Exception {
		
		System.out.println("/getPurchase.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	 
	/*
	@RequestMapping("/getPurchase.do")
	public String getPurchase2( @RequestParam("prodNo") String prodNo , Model model ) throws Exception {
		
		System.out.println("/getPurchase.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase2(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	*/
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView( @RequestParam("tranNo") String tranNo , Model model ) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase, 
			@RequestParam("tranNo") String tranNo, Model model) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		
		purchase.setTranNo(Integer.parseInt(tranNo));
		
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));

		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search, 
			HttpSession session, Model model ) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		User buyerId = (User) session.getAttribute("user");
		
		// Business logic 수행
		Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/updateTranCode.do")
	public String updateTranCode( @ModelAttribute("purchase") Purchase purchase, 
			@RequestParam("tranNo") String tranNo, @RequestParam("tranCode") String tranCode, Model model ) throws Exception {

		System.out.println("/updateTranCode.do");
		
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchase);
		model.addAttribute("purchase", purchase);
		
//		return "forward:/purchase/listPurchase.jsp";
		return "forward:/listPurchase.do";
	}
	
	@RequestMapping("/updateTranCodeByProd.do")
	public String updateTranCodeByProd( @ModelAttribute("purchase") Purchase purchase, 
			@RequestParam("prodNo") String prodNo, @RequestParam("tranCode") String tranCode, Model model ) throws Exception {
		
		System.out.println("/updateTranCodeByProd.do");

		purchase = purchaseService.getPurchase2(Integer.parseInt(prodNo));
		
		purchase.setTranNo(purchase.getTranNo());
		purchase.setTranCode(tranCode);
		
		System.out.println("[1]"+purchase.getTranNo());
		System.out.println("[2]"+purchase.getTranCode());
		
		purchaseService.updateTranCode(purchase);
		model.addAttribute("purchase", purchase);
		
//		return "forward:/purchase/listProduct.do?menu=manage";
		return "forward:/listProduct.do?menu=manage";
	}
	
	
}