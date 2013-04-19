apache-isis-ide
===============

**This project is alpha quality**

This project provides a set of plugins for Eclipse to help write domain-driven applications using [Apache Isis](http://incubator.apache.org/isis "Apache Isis").

There are three main components:

- enhanced rename refactoring
- views
- code folding

The plugin archive site can be downloaded [here](https://raw.github.com/danhaywood/apache-isis-ide/master/downloads/com.halware.nakedide.eclipse.site.zip "here").

Enhanced Refactoring
====================

The Isis programming model (see [cheat sheet](http://incubator.apache.org/isis/IsisCheatSheet.pdf "Isis programming model cheat sheet")) defines a number of supporting methods for each class member.  For example, for a property <tt>lastName</tt> defined by <tt>getLastName()</tt> and <tt>setLastName(String)</tt>, this proposed value can be validated using the supporting <tt>validateLastName(String)</tt> method.

The Apache Isis IDE plugins hook into the refactoring, so that any supporting methods are automatically renamed if the property (or collection, or action) is also renamed.  For example, changing <tt>lastName</tt> to <tt>familyName</tt> would result in the validate method being renamed to <tt>validateFamilyName(String)</tt>.

Views
=====

The views show summary information about the class members of the domain object opened in the current editor.  Double clicking on any row moves the cursor to that member in the editor.

The views are dynamically updated as you edit the class members; or (with the exception of the outline view) you can use view to edit the class member annotations.


Outline View
------------

The outline is a read-only view of all of the class members of the current domain object, indicating whether each member has supporting methods or not.  Double clicking on any row moves the cursor to that member in the editor.

![Outline View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/view-outline.png)

where:

<dl>
<dt>Hi</dt><dd>hideXxx() method is present</dd>
<dt>Di</dt><dd>disableXxx() method is present</dd>
<dt>Mo</dt><dd>modifyXxx() method is present</dd>
<dt>Cl</dt><dd>clearXxx() method is present</dd>
<dt>Va</dt><dd>validateXxx() method is present</dd>
<dt>AT</dt><dd>addToXxx() method is present</dd>
<dt>VAT</dt><dd>validateAddToXxx() method is present</dd>
<dt>RF</dt><dd>removeFromXxx() method is present</dd>
<dt>VRF</dt><dd>validateRemoveFromXxx() method is present</dd>
<dt>De</dt><dd>defaultXxx() method is present</dd>
<dt>Ch</dt><dd>choicesXxx() method is present</dd>
</dl>


Properties View
---------------

The properties view is a read/write view on the annotations relevant to properties of any type.  Double clicking on any row moves the cursor to that member in the editor.

![Properties View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/view-properties.png)

where:

<dl>
<dt>MO#sequence</dt><dd>@MemberOrder sequence() attribute</dd>
<dt>MO#name</dt><dd>@MemberOrder name() attribute</td></tr>
<dt>Title#sequence</dt><dd>@Title sequence() attribute</dd>
<dt>Title#preprend</dt><dd>@Title prepend() attribute</dd>
<dt>Title#append</dt><dd>@Title append() attribute</dd>
<dt>Title#abbreviatedTo</dt><dd>@Title abbreviatedTo() attribute</dd>
<dt>TypicalLength</dt><dd>@TypicalLength value() attribute</dd>
<dt>Disabled</dt><dd>@Disabled value() attribute</dd>
<dt>Optional</dt><dd>@Optional value() attribute</dd>
<dt>NotPersisted</dt><dd>@NotPersisted value() attribute</dd>
<dt>Hidden</dt><dd>@Hidden value() attribute</dd>
<dt>Named</dt><dd>@Named value() attribute</dd>
<dt>DescribedAs</dt><dd>@DescribedAs value() attribute</dd>
</dl>


String Properties View
----------------------

The string properties view is a read/write view on the annotations relevant to only properties of type java.lang.String.  Double clicking on any row moves the cursor to that member in the editor.

![String Properties View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/view-string-properties.png)

where:

<dl>
<dt>Type</dt><dd>Indicates the property's type</dd>
<dt>MaxLength</dt>@MaxLength value() attribute<dd></dd>
<dt>ML#numberOfLines</dt><dd>@MultiLine numberOfLines() attribute</dd>
<dt>ML#preventWrapping</dt><dd>@MultiLine preventWrapping() attribute</dd>
<dt>Mask</dt><dd>@Mask value() attribute</dd>
<dt>RE#validation</dt><dd>@RegEx validation() attribute</dd>
<dt>RE#format</dt><dd>@RegEx format() attribute</dd>
<dt>RE#caseSensitive</dt><dd>@RegEx caseSensitive() attribute</dd>
</dl>


Collections View
----------------

The collections view is a read/write view on the annotations relevant to collections.  Double clicking on any row moves the cursor to that collection in the editor.

![Collections View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/view-collections.png)

where:

<dl>
<dt>Type</dt><dd>Indicates the collection's type</dd>
<dt>MO#sequence</dt><dd>@MemberOrder sequence() attribute</dd>
<dt>MO#name</dt><dd>@MemberOrder name() attribute</td></tr>
<dt>TypeOf</dt><dd>@TypeOf value() attribute</dd>
<dt>Disabled</dt><dd>@Disabled value() attribute</dd>
<dt>NotPersisted</dt><dd>@NotPersisted value() attribute</dd>
<dt>Hidden</dt><dd>@Hidden value() attribute</dd>
<dt>Named</dt><dd>@Named value() attribute</dd>
<dt>DescribedAs</dt><dd>@DescribedAs value() attribute</dd>
</dl>


Actions View
----------------

The actions view is a read/write view on the annotations relevant to actions, also showing annotations for the actions' parameters (if any).  Double clicking on any row moves the cursor to that action in the editor.

![Actions View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/view-actions.png)

where, for the action:

<dl>
<dt>Type</dt><dd>Indicates the action's return type</dd>
<dt>MO#sequence</dt><dd>@MemberOrder sequence() attribute</dd>
<dt>MO#name</dt><dd>@MemberOrder name() attribute</td></tr>
<dt>QueryOnly</dt><dd>@QueryOnly value() attribute</dd>
<dt>Idempotent</dt><dd>@Idempotent value() attribute</dd>
<dt>Disabled</dt><dd>@Disabled value() attribute</dd>
<dt>Hidden</dt><dd>@Hidden value() attribute</dd>
<dt>NotContributed</dt><dd>@NotContributed value() attribute</dd>
<dt>NotInServiceMenu</dt><dd>@NotInServiceMenu value() attribute</dd>
<dt>Debug</dt><dd>@Debug value() attribute</dd>
<dt>Exploration</dt><dd>@Exploration value() attribute</dd>
<dt>Named</dt><dd>@Named value() attribute</dd>
<dt>DescribedAs</dt><dd>@DescribedAs value() attribute</dd>
</dl>

and where, for each action paramter:

<dl>
<dt>Type</dt><dd>Indicates the parameter's type</dd>
<dt>Named</dt><dd>@Named value() attribute</dd>
<dt>Optional</dt><dd>@Optional value() attribute</dd>
<dt>TypicalLength</dt><dd>@TypicalLength value() attribute</dd>
<dt>MaxLength</dt>@MaxLength value() attribute (string parameters only)<dd></dd>
<dt>ML#numberOfLines</dt><dd>@MultiLine numberOfLines() attribute (string parameters only)</dd>
<dt>ML#preventWrapping</dt><dd>@MultiLine preventWrapping() attribute (string parameters only)</dd>
<dt>Mask</dt><dd>@Mask value() attribute (string parameters only)</dd>
<dt>RE#validation</dt><dd>@RegEx validation() attribute (string parameters only)</dd>
<dt>RE#format</dt><dd>@RegEx format() attribute (string parameters only)</dd>
<dt>RE#caseSensitive</dt><dd>@RegEx caseSensitive() attribute (string parameters only)</dd>
</dl>


Code Folding
============

The code folding is derived from the Coffee Bytes folding plugin, fixed to run with the latest Eclipse.

Configure using the Preferences menu.

![Outline View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/codefolding-1.png)

![Outline View](https://raw.github.com/danhaywood/apache-isis-ide/master/website/codefolding-2.png)

