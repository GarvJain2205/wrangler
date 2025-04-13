package io.cdap.wrangler.plugin;

import io.cdap.wrangler.api.Executor;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.Collections;
import java.util.List;

/**
 * A plugin executor that aggregates byte sizes and durations over multiple rows.
 */
public class AggregateStats implements Executor<List<Row>, List<Row>> {

  private String sizeCol, timeCol, outSizeCol, outTimeCol;
  private long totalBytes = 0, totalMillis = 0;

  // Removed @Override since define() isn't in the Executor interface
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
    builder.define("sizeCol", TokenType.COLUMN_NAME);
    builder.define("timeCol", TokenType.COLUMN_NAME);
    builder.define("outSizeCol", TokenType.COLUMN_NAME);
    builder.define("outTimeCol", TokenType.COLUMN_NAME);
    return builder.build();
  }

  @Override
  public void initialize(Arguments args) {
    sizeCol = ((ColumnName) args.value("sizeCol")).value();
    timeCol = ((ColumnName) args.value("timeCol")).value();
    outSizeCol = ((ColumnName) args.value("outSizeCol")).value();
    outTimeCol = ((ColumnName) args.value("outTimeCol")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext ctx) {
    for (Row row : rows) {
      Object size = row.getValue(sizeCol);
      Object time = row.getValue(timeCol);
      if (size != null) {
        totalBytes += new ByteSize(size.toString()).getBytes();
      }
      if (time != null) {
        totalMillis += new TimeDuration(time.toString()).getMilliseconds();
      }
    }
    return Collections.emptyList();
  }

  // Removed @Override since finish() likely isn't in the Executor interface
  public List<Row> finish(ExecutorContext ctx) {
    Row result = new Row();
    result.add(outSizeCol, totalBytes / (1024.0 * 1024)); // MB
    result.add(outTimeCol, totalMillis / 1000.0);         // sec
    return Collections.singletonList(result);
  }

  @Override
  public void destroy() {
    // No resources to cleanup
  }
}
