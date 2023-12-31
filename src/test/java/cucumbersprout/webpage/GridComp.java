package cucumbersprout.webpage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridComp extends BasePage{

    public GridComp(WebDriver driver) {
        super(driver);
    }

    private final By table = By.cssSelector("div#Grid1.awe-grid.awe-hh.awe-ltr");
    private final By tableColumnHeaderNames = By.cssSelector("tbody.awe-hrow > tr > td >div");
    private final By tableRow = By.cssSelector("tr[data-g].awe-row");
    private final By componentLabel = By.cssSelector("div.example > h2");

    public void PrintCellAndColumn(String rowId){
        System.out.println("Input Id number: " + rowId);
        MergeTableHeaderAndRow(rowId).forEach((key, value) -> System.out.println(key + ": " + value.getText()));
    }

    public WebElement ScrollToComponent(String label){
        WebElement eLabel = driver.findElements(componentLabel)
                .stream().filter(l -> l.getText().contains(label)).findFirst().orElse(null);

        return ScrollIntoView(eLabel);
    }

    public WebElement ScrollToRow(String rowId){
        WebElement row = ScrollIntoView(driver.findElement(table))
                .findElements(tableRow)
                .stream()
                .filter(e -> e.getAttribute("data-k").equals(rowId)).findAny().get();

        return ScrollIntoView(row);
    }

    private Map<String, WebElement> MergeTableHeaderAndRow(String rowId){
        List<String> columnNames = driver.findElement(table)
                .findElements(tableColumnHeaderNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        List<WebElement> rowCells = ScrollToRow(rowId)
                .findElements(By.cssSelector("td"));

        return IntStream.range(0, columnNames.size()).boxed()
                .collect(Collectors.toMap(columnNames::get, rowCells::get, (x, y) -> y, LinkedHashMap::new));
    }

}
