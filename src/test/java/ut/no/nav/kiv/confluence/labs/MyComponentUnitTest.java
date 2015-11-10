package ut.no.nav.kiv.confluence.labs;

import com.atlassian.confluence.util.HtmlUtil;
import com.google.common.collect.Lists;
import no.nav.kiv.confluence.labs.api.MyPluginComponent;
import no.nav.kiv.confluence.labs.impl.MyPluginComponentImpl;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }

    @Test
    public void testJoiner() throws Exception {
        List<String> partQueries = Lists.newArrayList();

        partQueries.add("jeden");
        partQueries.add("dwa");
        partQueries.add("trzy");
        partQueries.add("cztery");
        partQueries.add("piec");
        partQueries.add("szesc");

        String resutl = partQueries.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(HtmlUtil.urlEncode(", ", "UTF-8")));

        System.out.print(resutl);

        assertTrue(resutl.equals("'jeden'%2C+'dwa'%2C+'trzy'%2C+'cztery'%2C+'piec'%2C+'szesc'"));

    }
}