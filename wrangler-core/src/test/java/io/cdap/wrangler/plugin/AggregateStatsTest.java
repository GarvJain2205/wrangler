package io.cdap.wrangler.plugin;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

  @Test
  public void testAggregation() throws Exception {
    List<Row> input = Arrays.asList(
      new Row("data", "1MB").add("time", "500ms"),
      new Row("data", "512KB").add("time", "1500ms"),
      new Row("data", "2MB").add("time", "2s")
    );

    String[] recipe = {
      "aggregate-stats :data :time :total_mb :total_sec"
    };

    List<Row> result = TestingRig.execute(recipe, input);

    Assert.assertEquals(1, result.size());

    Row row = result.get(0);
    Assert.assertEquals(3.5, (double) row.getValue("total_mb"), 0.01);
    Assert.assertEquals(4.0, (double) row.getValue("total_sec"), 0.01);
  }
}
