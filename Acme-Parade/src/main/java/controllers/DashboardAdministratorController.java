
package controllers;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ChapterService;
import services.FinderService;
import services.InceptionRecordService;
import services.MarchService;
import services.MemberService;
import services.ParadeService;
import services.PositionService;
import domain.Brotherhood;
import domain.Finder;
import domain.March;
import domain.Member;
import domain.Parade;
import domain.Position;

@Controller
@RequestMapping(value = "statistics/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services

	@Autowired
	private MemberService			memberService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MarchService			marchService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private ChapterService			chapterService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final Locale locale) {

		final ModelAndView result;
		final Collection<Member> members = this.memberService.findAll();
		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();
		Collection<Parade> parades;
		parades = this.paradeService.findAll();
		final Collection<March> marchs = this.marchService.findAll();

		final Collection<Finder> finders = this.finderService.findAll();

		Collection<Position> positions;
		positions = this.positionService.findAll();

		Double averageMemberPerBrotherhood;
		Double minMemberPerBrotherhood;
		Double maxMemberPerBrotherhood;
		Double stdevMemberPerBrotherhood;
		Collection<Member> acceptedMembers;

		Brotherhood largestBrotherhood;
		Brotherhood smallestBrotherhood;

		Double ratioApprovedRequests;
		Double ratioRejectedRequests;
		Double ratioPendingRequests;
		Double[] ratioApprovedRequestsInAparade;
		Double[] ratioRejectedInAparade;
		Double[] ratioPendingInAparade;

		// Integer[] histogram;
		Collection<Integer> countPositions;
		countPositions = this.positionService.countPositions();
		Collection<Parade> earlyparades;
		Collection<String> nameEsPositions;
		Collection<String> nameEnPositions;

		nameEsPositions = this.positionService.nameEsPositions();
		nameEnPositions = this.positionService.nameEnPositions();

		Double[] statsFinder;
		Double ratioFinders;

		Double maxBrotherhoodPerArea;
		Double minBrotherhoodPerArea;
		Double ratioBrotherhoodsPerArea;
		Double countBrotherhoodsPerArea;
		Double stdevBrotherhoodPerArea;

		String language;
		language = locale.getLanguage();

		statsFinder = this.finderService.statsFinder();
		ratioFinders = this.finderService.ratioFinders();

		maxBrotherhoodPerArea = this.brotherhoodService.maxBrotherhoodPerArea();
		minBrotherhoodPerArea = this.brotherhoodService.minBrotherhoodPerArea();
		ratioBrotherhoodsPerArea = this.brotherhoodService.ratioBrotherhoodsPerArea();
		countBrotherhoodsPerArea = this.brotherhoodService.countBrotherhoodsPerArea();
		stdevBrotherhoodPerArea = this.brotherhoodService.stdevBrotherhoodPerArea();

		averageMemberPerBrotherhood = this.memberService.averageMemberPerBrotherhood();
		minMemberPerBrotherhood = this.memberService.minMemberPerBrotherhood();
		maxMemberPerBrotherhood = this.memberService.maxMemberPerBrotherhood();
		stdevMemberPerBrotherhood = this.memberService.stdevMembersPerBrotherhood();
		acceptedMembers = this.memberService.acceptedMembers();

		largestBrotherhood = this.brotherhoodService.largestBrotherhood();
		smallestBrotherhood = this.brotherhoodService.smallestBrotherhood();

		ratioApprovedRequests = this.marchService.ratioApprovedRequests();
		ratioRejectedRequests = this.marchService.ratioRejectedRequests();
		ratioPendingRequests = this.marchService.ratioPendingRequests();
		ratioApprovedRequestsInAparade = this.marchService.ratioApprovedInAParade();
		ratioRejectedInAparade = this.marchService.ratioRejectedInAParade();
		ratioPendingInAparade = this.marchService.ratioPendingInAParade();

		earlyparades = this.paradeService.findEarlyParades();

		// histogram = this.positionService.histogram();

		final Double minRecordsPerHistory = this.inceptionRecordService.minRecordsPerHistory();
		final Double maxRecordsPerHistory = this.inceptionRecordService.maxRecordsPerHistory();
		final Double avgRecordsPerHistory = this.inceptionRecordService.avgRecordsPerHistory();
		final Double stedvRecordsPerHistory = this.inceptionRecordService.stedvRecordsPerHistory();
		final Collection<Brotherhood> largerBrosthanAvg = this.inceptionRecordService.largerBrosthanAvg();
		final String getLargestBrotherhood = this.inceptionRecordService.getLargestBrotherhood();
		Collection<Double> ratioFinalModeGroupedByStatus =this.paradeService.ratioFinalModeGroupedByStatus();
		result = new ModelAndView("administrator/statistics");

		result.addObject("getLargestBrotherhood", getLargestBrotherhood);
		result.addObject("largerBrosthanAvg", largerBrosthanAvg);
		result.addObject("maxRecordsPerHistory", maxRecordsPerHistory);
		result.addObject("minRecordsPerHistory", minRecordsPerHistory);
		result.addObject("avgRecordsPerHistory", avgRecordsPerHistory);
		result.addObject("stedvRecordsPerHistory", stedvRecordsPerHistory);

		result.addObject("stdevBrotherhoodPerArea", stdevBrotherhoodPerArea);
		result.addObject("nameEsPositions", nameEsPositions);
		result.addObject("nameEnPositions", nameEnPositions);
		result.addObject("finders", finders);
		result.addObject("members", members);
		result.addObject("bros", bros);
		result.addObject("marchs", marchs);
		result.addObject("positions", positions.size());
		// result.addObject("histogram", histogram);
		result.addObject("countPositions", countPositions);

		result.addObject("earlyparades", earlyparades);

		result.addObject("ratioApprovedRequests", ratioApprovedRequests);
		result.addObject("ratioRejectedRequests", ratioRejectedRequests);
		result.addObject("ratioPendingRequests", ratioPendingRequests);
		result.addObject("ratioApprovedparade", ratioApprovedRequestsInAparade);
		result.addObject("ratioRejectedInAparade", ratioRejectedInAparade);
		result.addObject("ratioPendingInAparade", ratioPendingInAparade);

		result.addObject("largestBrotherhood", largestBrotherhood);
		result.addObject("smallestBrotherhood", smallestBrotherhood);

		result.addObject("acceptedMembers", acceptedMembers);
		result.addObject("maxMemberPerBrotherhood", maxMemberPerBrotherhood);
		result.addObject("minMemberPerBrotherhood", minMemberPerBrotherhood);
		result.addObject("averageMemberPerBrotherhood", averageMemberPerBrotherhood);
		result.addObject("stdevMemberPerBrotherhood", stdevMemberPerBrotherhood);

		result.addObject("statsFinder", statsFinder);
		result.addObject("ratioFinders", ratioFinders);

		result.addObject("maxBrotherhoodPerArea", maxBrotherhoodPerArea);
		result.addObject("minBrotherhoodPerArea", minBrotherhoodPerArea);
		result.addObject("ratioBrotherhoodsPerArea", ratioBrotherhoodsPerArea);
		result.addObject("countBrotherhoodsPerArea", countBrotherhoodsPerArea);

		result.addObject("parades", parades.size());
		result.addObject("language", language);

		result.addObject("ratioAreasNotManaged", this.chapterService.ratioAreasNotManaged());
		result.addObject("paradeChapterStatistics", this.chapterService.paradeChapterStatistics());
		result.addObject("10percentMore", this.chapterService.chapter10percentMore());
		result.addObject("ratioDraftVsFinal", this.paradeService.ratioDraftVsFinal());
		result.addObject("ratioFinalModeGroupedByStatus", ratioFinalModeGroupedByStatus);

		result.addObject("requestURI", "statistics/administrator/display.do");
		return result;

	}
}
