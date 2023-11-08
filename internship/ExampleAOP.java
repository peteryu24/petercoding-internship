package geomex.common.logger;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import geomex.common.access.service.MtAccessLogService;
import geomex.common.access.service.MtAccessLogVO;
import geomex.common.data.AuthToken;
import geomex.common.user.service.MtUsrDescVO;
import geomex.esupport.cont.service.CtContHistService;
import geomex.esupport.cont.service.CtContHistVO;
import geomex.esupport.order.service.CtOrdersService;
import geomex.esupport.order.service.CtOrdersVO;
import geomex.esupport.outs.service.CtOutsService;
import geomex.esupport.outs.service.CtOutsVO;
import geomex.esupport.proj.service.CtProjHistService;
import geomex.esupport.proj.service.CtProjHistVO;
import geomex.esupport.proj.service.CtProjService;
import geomex.esupport.proj.service.CtProjVO;
import geomex.esupport.sale.service.CtSaleHistService;
import geomex.esupport.sale.service.CtSaleHistVO;
import geomex.esupport.sale.service.CtSaleService;
import geomex.esupport.sale.service.CtSaleVO;
import geomex.tf.utils.DateUtil;

@Aspect
@Component
public class CtLoggerAspect {
	protected Log log = LogFactory.getLog(CtLoggerAspect.class);
	
	
	private final String METHOD_PROJ_UPD = "updateCtProj";
	private final String METHOD_SALES_UPD = "updateCtSale";
	private final String METHOD_ORDER_UPD = "updateCtOrders";
	private final String METHOD_OUTS_UPD = "updateCtOuts";
	
	private final String METHOD_PROJ_ADD = "insertCtProj";
	private final String METHOD_SALES_ADD = "insertCtSale";
	private final String METHOD_ORDER_ADD = "insertCtOrders";
	private final String METHOD_OUTS_ADD = "insertCtOuts";
	
	private final String METHOD_LOGIN = "plainLogin";

	
    @Resource(name = "ctProjHistService")
    private CtProjHistService ctProjHistService;
    
    @Resource(name = "ctLoggerConvert")
    private CtLoggerConvert ctLoggerConvert;
    
    @Resource(name = "ctProjService")
    private CtProjService ctProjService;
    
    @Resource(name = "ctSaleHistService")
    private CtSaleHistService ctSaleHistService;
    
    
    @Resource(name = "ctContHistService")
    private CtContHistService ctContHistService;
    
    @Resource(name = "ctSaleService")
    private CtSaleService ctSaleService;
    
    @Resource(name = "ctOrdersService")
    private CtOrdersService ctOrdersService;
    
    @Resource(name = "ctOutsService")
    private CtOutsService ctOutsService;

    @Resource(name = "mtAccessLogService")
    private MtAccessLogService mtAccessLogService;

    @AfterReturning(pointcut = "execution(* geomex.common.auth.web.*.*(..))", returning = "result")
    public void accControllerExecution(JoinPoint joinPoint, Object result) {
    	String methodName = joinPoint.getSignature().getName();
    	System.out.println("Result: " + result);
    	System.out.println("Result: " + joinPoint.getArgs());
    	if ( !isOk(result.toString()) ) return;
    	
    	 // 컨트롤러 메서드 실행 이전에 파라미터를 가져올 수 있습니다.
        Object[] args = joinPoint.getArgs();
        
    	
    	try {
    		if ( methodName.equals(METHOD_LOGIN)) {
    			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    			String ipAddress = attributes.getRequest().getRemoteAddr();
    			//로그인 처리
    			MtUsrDescVO userVo = (MtUsrDescVO) args[0];
    			MtAccessLogVO accVo = new MtAccessLogVO();
    			
    			accVo.setAllowYn("Y");
    			accVo.setUserId(userVo.getUserId());
    			accVo.setUseTime(DateUtil.getStrSec());
    			accVo.setConnIp(ipAddress);
    			mtAccessLogService.insertMtAccessLog(accVo);
    		}
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    }
    
    @AfterReturning(pointcut = "execution(* geomex.esupport.*.web.*.*(..))", returning = "result")
    public void logControllerExecution(JoinPoint joinPoint, Object result) {

    	String methodName = joinPoint.getSignature().getName();
    	
    	//System.out.println("Result: " + result);
    	//System.out.println("Result: " + joinPoint.getArgs());
    	if ( !isOk(result.toString()) ) return;
    	
    	 // 컨트롤러 메서드 실행 이전에 파라미터를 가져올 수 있습니다.
        Object[] args = joinPoint.getArgs();
    	try {
    		
    		switch(methodName) {
    		
	    		case METHOD_PROJ_ADD :
	    			CtProjHistVO histVo = new CtProjHistVO();
	    			CtProjVO vo = (CtProjVO) args[0];
	    			
	    			if ( methodName.equals(METHOD_PROJ_UPD) ) {
	    				CtProjVO oldVo = ctProjService.selectCtProj(vo);	    				 
	    				histVo.setHistMsg(ctLoggerConvert.convertMap(oldVo, vo));
	    				histVo.setProjCd(vo.getProjCd());
	    			} else {
	    				histVo.setProjCd(getResultMsg(result.toString(), "message"));
	    			}
	    			
					histVo.setHistCd(methodName.equals(METHOD_PROJ_UPD) ? ctLoggerConvert.HIST_CD_UPDATE : ctLoggerConvert.HIST_CD_ADD );
					//||
					histVo.setCreDat(DateUtil.getStrSec());
					histVo.setUserId(AuthToken.getUserId(vo.getToken()));
	    			
	    			ctProjHistService.insertCtProjHist(histVo);
	    	        
	    			break;
	    		
	    		case METHOD_SALES_ADD :
	    			CtSaleVO saleVo = (CtSaleVO) args[0];
	    			CtSaleHistVO saleHistVo = new CtSaleHistVO();
	    			
	    			if ( methodName.equals(METHOD_SALES_UPD) ) {
	    				CtSaleVO oldSaleVo = ctSaleService.selectCtSale(saleVo);	    				
	    				saleHistVo.setHistMsg(ctLoggerConvert.convertMap(oldSaleVo, saleVo)); 
	    				saleHistVo.setSaleCd(saleVo.getSaleCd());
	    				
	    			} else {
	    				saleHistVo.setSaleCd(getResultMsg(result.toString(), "message"));
	    			}
	    			
	    			saleHistVo.setHistCd(methodName.equals(METHOD_SALES_UPD) ? ctLoggerConvert.HIST_CD_UPDATE : ctLoggerConvert.HIST_CD_ADD );
	    		
					saleHistVo.setCreDat(DateUtil.getStrSec());
					saleHistVo.setUserId(AuthToken.getUserId(saleVo.getToken()));
	    			
					ctSaleHistService.insertCtSaleHist(saleHistVo);
	    	        
	    			break;
	
	    		case METHOD_ORDER_ADD :
	    			CtOrdersVO orderVo = (CtOrdersVO) args[0];
	    			CtContHistVO contHistVo = new CtContHistVO();
	    			
	    			if ( methodName.equals(METHOD_ORDER_UPD) ) {
	    				CtOrdersVO oldOrderVo = ctOrdersService.selectCtOrders(orderVo);
	    				contHistVo.setHistMsg(ctLoggerConvert.convertMap(oldOrderVo, orderVo));
	    				contHistVo.setContCd(orderVo.getContCd());
	    				
	    			} else {
	    				contHistVo.setContCd(getResultMsg(result.toString(), "message"));
	    			}
	    			
	    			contHistVo.setHistCd(methodName.equals(METHOD_ORDER_UPD) ? ctLoggerConvert.HIST_CD_UPDATE : ctLoggerConvert.HIST_CD_ADD );
	    	
	    			//CtContHistVO.setSaleCd(saleVo.getSaleCd());
	    			contHistVo.setContTyp("1");
	    			
	    			contHistVo.setCreDat(DateUtil.getStrSec());
	    			contHistVo.setUserId(AuthToken.getUserId(orderVo.getToken()));
	    			
	    			ctContHistService.insertCtContHist(contHistVo);
					
	    			break;
	    	
	    		case METHOD_OUTS_ADD :
	    			CtOutsVO outsVo = (CtOutsVO) args[0];
	    			CtContHistVO contoHistVo = new CtContHistVO();
	    			
	    			if ( methodName.equals(METHOD_OUTS_UPD) ) {
	    				CtOutsVO oldOutsVo = ctOutsService.selectCtOuts(outsVo);
	    				contoHistVo.setHistMsg(ctLoggerConvert.convertMap(oldOutsVo, outsVo));
	    				contoHistVo.setContCd(outsVo.getContCd());
	    			} else {
	    				contoHistVo.setContCd(getResultMsg(result.toString(), "message"));
	    			}
	    			
	    			contoHistVo.setHistCd(methodName.equals(METHOD_OUTS_UPD) ? ctLoggerConvert.HIST_CD_UPDATE : ctLoggerConvert.HIST_CD_ADD );
	    	
	    			//CtContHistVO.setSaleCd(saleVo.getSaleCd());
	    			contoHistVo.setContTyp("2");
	    			contoHistVo.setCreDat(DateUtil.getStrSec());
	    			contoHistVo.setUserId(AuthToken.getUserId(outsVo.getToken()));
	    			
	    			ctContHistService.insertCtContHist(contoHistVo);
					
	    			break;
	    		
    		}
    		
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // result를 이용하여 필요한 정보를 추출하고 DB에 이력을 남깁니다.
    }
    

    private String getResultMsg( String msg, String key ) {
    	
    	if ( msg.indexOf("\""+key+"\"") != -1 ) {
    		String str = msg.split("\""+key+"\":\"")[1];
    		return str.substring(0, str.indexOf("\"")).replace("insert:", "");
    	} 
    	
    	return "";
    }
    private boolean isOk(String msg) {
    	
    	if ( msg.indexOf("\"status\"") != -1 ) {
    		String str = msg.split("\"status\":\"")[1];
    		return str.substring(0, str.indexOf("\"")).equals("ok");
    	} 
    	return false;
    }
    
    
}
